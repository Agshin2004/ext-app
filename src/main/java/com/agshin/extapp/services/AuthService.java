package com.agshin.extapp.services;

import com.agshin.extapp.model.entities.User;

public interface AuthService {
    User getCurrentUser();

    Long getCurrentUserId();

}
