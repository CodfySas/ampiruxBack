package com.osia.template.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

class SubmitFile {

    fun reviewExt(ext: String) {
        val letExtensions =
            mutableListOf("doc", "docx", "ppt", "pptx", "xls", "xlsx", "png", "jpg", "jpeg", "gif", "pdf")
        if (!letExtensions.contains(ext)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    fun submitPlanning(
        name: String,
        extension: String?,
        file: MultipartFile
    ) = try {
        val log = LoggerFactory.getLogger(javaClass)
        val targetLocation: Path = Path.of("src/main/resources/plannings/$name.$extension")
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        val imageBytes = Files.readAllBytes(targetLocation)
        ResponseEntity.ok().contentType(determineMediaType(extension ?: ""))
            .body(imageBytes)
    } catch (ex: Exception) {
        val log = LoggerFactory.getLogger(javaClass)
        log.info(ObjectMapper().writeValueAsString(ex))
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar el archivo: ${ex.message}")
    }

    fun submitFile(
        uuid: UUID,
        extension: String?,
        file: MultipartFile
    ) = try {
        val targetLocation: Path = Path.of("src/main/resources/files/$uuid.$extension")
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        val imageBytes = Files.readAllBytes(targetLocation)
        ResponseEntity.ok().contentType(determineMediaType(extension ?: ""))
            .body(imageBytes)
    } catch (ex: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar el archivo: ${ex.message}")
    }

    fun determineMediaType(ext: String): MediaType {
        return when {
            ext.endsWith("png") -> MediaType.IMAGE_PNG
            ext.endsWith("jpg") || ext.endsWith("jpeg") -> MediaType.IMAGE_JPEG
            ext.endsWith("gif") -> MediaType.IMAGE_GIF
            ext.endsWith("pdf") -> MediaType.APPLICATION_PDF
            ext.endsWith("doc") || ext.endsWith("docx") -> MediaType.valueOf("application/msword")
            ext.endsWith("xls") || ext.endsWith("xlsx") -> MediaType.valueOf("application/vnd.ms-excel")
            ext.endsWith("ppt") || ext.endsWith("pptx") -> MediaType.valueOf("application/vnd.ms-powerpoint")
            else -> MediaType.APPLICATION_OCTET_STREAM // Tipo genérico para otros tipos de archivo
        }
    }
}
