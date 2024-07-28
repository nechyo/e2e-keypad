package bob.e2e.domain.service

import bob.e2e.domain.model.E2eKeypad
import bob.e2e.presentation.dto.E2eKeypadRequestDto
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*

@Service
class E2eKeypadService {
    fun getE2eKeypadImages(): MutableList<E2eKeypad>? {
        val path = Paths.get("src/main/resources/keypad")
        val filesContent = mutableListOf<E2eKeypad>()

        try {
            Files.walk(path)
                .filter { Files.isRegularFile(it) }
                .forEach { filePath ->
                    val resource: Resource = ClassPathResource("keypad/${filePath.fileName}")
                    if (resource.exists() && resource.isReadable) {
                        val content = resource.inputStream.use { inputStream ->
                            val bytes = StreamUtils.copyToByteArray(inputStream)
                            val base64Data = Base64.getEncoder().encodeToString(bytes)
                            "data:image/png;base64,$base64Data"
                        }
                        val fileName = filePath.fileName.toString()
                        val id =  fileName.substringAfter('_').substringBefore('.')

                        val bytes = content.toByteArray()
                        val md = MessageDigest.getInstance("SHA-256")
                        val digest = md.digest(bytes)
                        val hash = digest.fold("") { str, it -> str + "%02x".format(it) }

                        filesContent.add(E2eKeypadRequestDto(id).toEntity(hash, content))
                    }
                }
        } catch (e: Exception) {
            return null
        }

        return filesContent
    }
}