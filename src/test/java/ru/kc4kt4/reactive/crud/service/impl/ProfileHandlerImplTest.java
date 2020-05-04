package ru.kc4kt4.reactive.crud.service.impl;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = ProfileHandlerImpl.class)
class ProfileHandlerImplTest {
    @Autowired
    private ProfileHandlerImpl handler;
    @MockBean
    private ProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @AfterEach
    void verifyNoMore() {
        verifyNoMoreInteractions(service);
    }

    @Test
    @Ignore
    void getById() {
    }

    @Test
    @Ignore
    void all() {
    }

    @Test
    @Ignore
    void deleteById() {
    }

    @Test
    @Ignore
    void save() {
    }
}