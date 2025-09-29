package com.example.news.back.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long doc_id;


}
