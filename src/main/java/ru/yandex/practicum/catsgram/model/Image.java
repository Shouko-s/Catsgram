package ru.yandex.practicum.catsgram.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
public class Image {
    private Long id;
    private long postI;
    private String originalFileName;
    private String filePath;
}
