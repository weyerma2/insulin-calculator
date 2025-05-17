package ch.zhaw.deeplearningjava.insulincalculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InsulincalculatorApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void testImageUpload_returnsNutritionData() throws Exception {
        // Beispielbild aus den Testressourcen laden
        InputStream imageStream = getClass().getResourceAsStream("/testdata/pizza.jpg");
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "pizza.jpg", MediaType.IMAGE_JPEG_VALUE, imageStream
        );

        mockMvc.perform(multipart("/api/upload")
                        .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carbs").exists())
                .andExpect(jsonPath("$.insulin").exists());
    }

}
