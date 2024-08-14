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
    fun getE2eKeypad(userId: String): E2eKeypadResponseDto {
        // userId 저장 (hashes 필요, 타입 정리 필요, 구현 완성)
        val classLoader = this::class.java.classLoader

        // 이미지를 불러옵니다.
        val numbers = (0..9).toMutableList().apply { shuffle() }

        // 블랭크 이미지를 두 번 추가합니다.
        val result = numbers.map { "_$it.png" }.toMutableList()
        repeat(2) {
            result.add("_blank.png")
        }

        // 다시 섞어서 블랭크 이미지의 위치가 무작위가 되도록 합니다.
        result.shuffle()
        val data = repository.findById(userId).orElseGet {
            val newHash = result.map { fileName ->
                if (fileName == "_blank.png") "" else generateRandomHash()
            }

            repository.save(E2eKeypadHash(id= userId, hashes = newHash, nums = result.toList()))
        }
        val images = data.nums.map { fileName ->
            ImageIO.read(classLoader.getResourceAsStream("keypad/$fileName"))
        }

        val rows = 3
        val cols = 4
        val width = images[0].width
        val height = images[0].height

        // Create a new image with the required dimensions
        val combinedImage = BufferedImage(width * cols, height * rows, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = combinedImage.createGraphics()
        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, width * cols, height * rows)


        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val index = y * cols + x
                if (index < images.size) {
                    g.drawImage(images[index], x * width, y * height, null)
                }
            }
        }

        g.dispose()

        // Convert the combined image to a Base64 string
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