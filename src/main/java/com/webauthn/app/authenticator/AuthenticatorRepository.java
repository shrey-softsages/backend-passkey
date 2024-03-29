package com.webauthn.app.authenticator;

import java.util.List;
import java.util.Optional;

import com.webauthn.app.user.AppUser;
import com.yubico.webauthn.data.ByteArray;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatorRepository extends JpaRepository<Authenticator, Long> {
    Optional<Authenticator> findByCredentialId(String credentialId);
    List<Authenticator> findAllByUser (AppUser user);
    List<Authenticator> findAllByCredentialId(String credentialId);
}
