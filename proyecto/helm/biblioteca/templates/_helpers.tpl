{{/*
_helpers.tpl (Capitulo 15): nombre completo de la
imagen de un servicio, combinando imageRegistry +
imageRepository + nombre del modulo + imageTag. Se
llama con un diccionario {global: ..., name: ...}
porque las plantillas Go no pueden ver ".Values"
directamente dentro de un "range" sin pasarlo.

Los espacios y saltos de linea entre tokens dentro
de "{{ }}" no importan para el motor de plantillas
de Go -- se pueden partir en varias lineas sin
cambiar el resultado, igual que en Java.
*/}}
{{- define "biblioteca.image" -}}
{{- printf
    "%s%s-%s:%s"
    .global.imageRegistry
    .global.imageRepository
    .name
    .global.imageTag
-}}
{{- end -}}
