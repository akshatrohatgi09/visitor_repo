package com.police.evisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserLoginIgnoreCaseAndRecordStatusNot(String userLogin, Character recordStatus);

	User findByUserEmailIgnoreCaseAndRecordStatusNot(String userEmail, Character recordStatus);

	User findByUserMobAndRecordStatusNot(String userMob, Character recordStatus);

}