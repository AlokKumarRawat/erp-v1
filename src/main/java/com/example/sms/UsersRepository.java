package com.example.sms;

import com.example.sms.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUseridAndPasswordAndRole(
            String userid,
            String password,
            String role
    );
}