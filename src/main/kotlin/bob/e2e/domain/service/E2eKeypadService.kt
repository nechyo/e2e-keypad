package bob.e2e.domain.service

import bob.e2e.data.repository.E2eKeypadRedisRepository
import bob.e2e.domain.model.E2eKeypadHash
import bob.e2e.presentation.dto.E2eKeypadResponseDto
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.security.SecureRandom
import java.util.*
import javax.imageio.ImageIO

@Service
class E2eKeypadService(
    private  val repository: E2eKeypadRedisRepository
) {
    fun getE2eKeypad(): E2eKeypadResponseDto {
        val classLoader = this::class.java.classLoader

        val numbers = (0..9).toMutableList().apply { shuffle() }

        val result = numbers.map { "_$it.png" }.toMutableList()
        repeat(2) {
            result.add("_blank.png")
        }

        result.shuffle()
        val data = repository.findById(generateRandomHash()).orElseGet { // 일단 테스트용
            val newHash = result.map { fileName ->
                if (fileName == "_blank.png") "" else generateRandomHash()
            }

            repository.save(E2eKeypadHash(id= generateRandomHash(), hashes = newHash, nums = result.toList()))
        }
        val images = data.nums.map { fileName ->
            ImageIO.read(classLoader.getResourceAsStream("keypad/$fileName"))
        }

        val rows = 3
        val cols = 4
        val width = images[0].width
        val height = images[0].height
        val gapX = 10
        val gapY = 10

        val combinedImageWidth = width * cols + gapX * (cols - 1)
        val combinedImageHeight = height * rows + gapY * (rows - 1)

        val combinedImage = BufferedImage(combinedImageWidth, combinedImageHeight, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = combinedImage.createGraphics()
        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, combinedImageWidth, combinedImageHeight)

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val index = y * cols + x
                if (index < images.size) {
                    val posX = x * (width + gapX)
                    val posY = y * (height + gapY)
                    g.drawImage(images[index], posX, posY, width, height, null)
                }
            }
        }

        g.dispose()



        val outputStream = ByteArrayOutputStream()
        ImageIO.write(combinedImage, "png", outputStream)
        val imageBytes = outputStream.toByteArray()
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        return  E2eKeypadResponseDto(data.hashes, "data:image/png;base64,$base64Image")
    }

    private fun generateRandomHash(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }
}