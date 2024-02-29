package com.osia.nota_maestro.controller.documentation.v1

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

@RestController("documentation.v1.crud")
@CrossOrigin
@RequestMapping("v1/documentation")
@Validated
class DocumentationController() {
    @PostMapping("/logo", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun submitLogo(@RequestParam("file") file: MultipartFile, @RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val fileName = "logo-$school.png"
            val targetLocation: Path = Path.of("src/main/resources/logos/$fileName")
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar la imagen: ${ex.message}")
        }
    }

    @PostMapping("/header", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun submitHeader(@RequestParam("file") file: MultipartFile, @RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val fileName = "header-$school.png"
            val targetLocation: Path = Path.of("src/main/resources/logos/$fileName")
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar la imagen: ${ex.message}")
        }
    }

    @PostMapping("/sign", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun submitSign(@RequestParam("file") file: MultipartFile, @RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val fileName = "sign-$school.png"
            val targetLocation: Path = Path.of("src/main/resources/logos/$fileName")
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar la imagen: ${ex.message}")
        }
    }

    @GetMapping("/logo")
    fun getLogo(@RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val targetLocation: Path = Path.of("src/main/resources/logos/logo-$school.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }

    @GetMapping("/header")
    fun getHeader(@RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val targetLocation: Path = Path.of("src/main/resources/logos/header-$school.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }

    @GetMapping("/sign")
    fun getSign(@RequestHeader school: UUID): ResponseEntity<ByteArray> {
        return try {
            val targetLocation: Path = Path.of("src/main/resources/logos/sign-$school.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }
}
