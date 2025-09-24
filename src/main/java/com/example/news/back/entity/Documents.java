package com.example.news.back.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long doc_id;


}
