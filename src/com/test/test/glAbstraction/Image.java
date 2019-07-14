package com.test.test.glAbstraction;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.test.test.glAbstraction.utils.IOUtil;

import java.io.*;
import java.nio.*;
import java.util.*;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

//code selectively copied from lwjgl demo at https://github.com/LWJGL/lwjgl3/blob/18975883e844d9dc53874836ec45257da13085d9/modules/samples/src/test/java/org/lwjgl/demo/stb/Image.java
public final class Image {

	private final ByteBuffer image;
	public final int texID;
	
	public final int width, height, comp;
	
	public Image(String path)
	{
		ByteBuffer imageBuffer;
		try {
			imageBuffer = IOUtil.ioResourceToByteBuffer(path, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try (MemoryStack stack = stackPush()) {
			IntBuffer width = stack.mallocInt(1);
	        IntBuffer height = stack.mallocInt(1);
	        IntBuffer comp = stack.mallocInt(1);
	        
	        stbi_set_flip_vertically_on_load(true);
	        image = stbi_load_from_memory(imageBuffer, width, height, comp, 0);
	        if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
	        }
	        
	        this.width = width.get(0);
	        this.height = height.get(0);
	        this.comp = comp.get(0);
		}
		
		this.texID = createTexture();
	}
	
	public void cleanup()
	{
		glDeleteTextures(this.texID);
	}
	
	private void premultiplyAlpha() {
        int stride = width * 4;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * stride + x * 4;

                float alpha = (image.get(i + 3) & 0xFF) / 255.0f;
                image.put(i + 0, (byte)round(((image.get(i + 0) & 0xFF) * alpha)));
                image.put(i + 1, (byte)round(((image.get(i + 1) & 0xFF) * alpha)));
                image.put(i + 2, (byte)round(((image.get(i + 2) & 0xFF) * alpha)));
            }
        }
	}
	
	private int createTexture() {
        int texID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        int format;
        if (comp == 3) {
            if ((width & 3) != 0) {
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            }
            format = GL_RGB;
        } else {
            premultiplyAlpha();

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

            format = GL_RGBA;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image);
        
        ByteBuffer input_pixels = image;
        int        input_w      = width;
        int        input_h      = height;
        int        mipmapLevel  = 0;
        while (1 < input_w || 1 < input_h) {
            int output_w = Math.max(1, input_w >> 1);
            int output_h = Math.max(1, input_h >> 1);

            ByteBuffer output_pixels = memAlloc(output_w * output_h * comp);
            stbir_resize_uint8_generic(
                input_pixels, input_w, input_h, input_w * comp,
                output_pixels, output_w, output_h, output_w * comp,
                comp, comp == 4 ? 3 : STBIR_ALPHA_CHANNEL_NONE, STBIR_FLAG_ALPHA_PREMULTIPLIED,
                STBIR_EDGE_CLAMP,
                STBIR_FILTER_MITCHELL,
                STBIR_COLORSPACE_SRGB
            );

            if (mipmapLevel == 0) {
                stbi_image_free(image);
            } else {
                memFree(input_pixels);
            }

            glTexImage2D(GL_TEXTURE_2D, ++mipmapLevel, format, output_w, output_h, 0, format, GL_UNSIGNED_BYTE, output_pixels);

            input_pixels = output_pixels;
            input_w = output_w;
            input_h = output_h;
        }
        if (mipmapLevel == 0) {
            stbi_image_free(image);
        } else {
            memFree(input_pixels);
        }
        

        return texID;
	}
	
}
