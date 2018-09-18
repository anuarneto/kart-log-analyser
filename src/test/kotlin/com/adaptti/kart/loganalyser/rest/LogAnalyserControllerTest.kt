package com.adaptti.kart.loganalyser.rest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext


@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogAnalyserControllerTest {

    @Autowired
    private lateinit var wac: WebApplicationContext

    lateinit var mockMvc: MockMvc

    @Mock
    private val logAnalyserController: LogAnalyserController? = null

    @Autowired
    private val template: TestRestTemplate? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        mockMvc = webAppContextSetup(wac).build()
    }

    @Test
    @Throws(Exception::class)
    fun testAnalyseCommon() {

        val mockMultipartFile = MockMultipartFile("file", javaClass.classLoader.getResourceAsStream("kart.log"))

        val builder = MockMvcRequestBuilders.multipart("/kart/log/analyse/json")
                .file(mockMultipartFile)

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.positions").isArray)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Throws(Exception::class)
    fun testAnalyseEdgeCases() {

        val mockMultipartFile = MockMultipartFile("file", javaClass.classLoader.getResourceAsStream("kart2.log"))

        val builder = MockMvcRequestBuilders.multipart("/kart/log/analyse/json")
                .file(mockMultipartFile)

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.positions").isArray)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Throws(Exception::class)
    fun testAnalyseHtml() {

        val mockMultipartFile = MockMultipartFile("file", javaClass.classLoader.getResourceAsStream("kart.log"))

        val builder = MockMvcRequestBuilders.multipart("/kart/log/analyse/html")
                .file(mockMultipartFile)

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Throws(Exception::class)
    fun testAnalysePlain() {

        val mockMultipartFile = MockMultipartFile("file", javaClass.classLoader.getResourceAsStream("kart.log"))

        val builder = MockMvcRequestBuilders.multipart("/kart/log/analyse")
                .file(mockMultipartFile)

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Throws(Exception::class)
    fun testAnalysePlainDetailed() {

        val mockMultipartFile = MockMultipartFile("file", javaClass.classLoader.getResourceAsStream("kart.log"))

        val builder = MockMvcRequestBuilders.multipart("/kart/log/analyse/details")
                .file(mockMultipartFile)

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andDo(MockMvcResultHandlers.print())
    }
}
