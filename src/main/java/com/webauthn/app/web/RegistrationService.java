package com.webauthn.app.web;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.webauthn.app.authenticator.Authenticator;
import com.webauthn.app.authenticator.AuthenticatorRepository;
import com.webauthn.app.user.AppUser;
import com.webauthn.app.user.UserRepository;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Repository
@Getter
@AllArgsConstructor
public class RegistrationService implements CredentialRepository  {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticatorRepository authRepository;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        AppUser user = userRepo.findByUsername(username);
        List<Authenticator> auth = authRepository.findAllByUser(user);
        return auth.stream()
        .map(
            credential ->
                PublicKeyCredentialDescriptor.builder()
                    .id(new ByteArray(Base64.getDecoder().decode(credential.getCredentialId())))
                    .build())
        .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        AppUser user = userRepo.findByUsername(username);
        return Optional.of(new ByteArray(Base64.getDecoder().decode(user.getHandle())));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        AppUser user = userRepo.findByHandle(userHandle.getBase64());
        return Optional.of(user.getUsername());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<Authenticator> auth = authRepository.findByCredentialId(credentialId.getBase64());
        System.out.println(auth);
        System.out.println(credentialId.getBase64()+"\n"+userHandle.getBase64());
        return auth.map(
            credential ->
                RegisteredCredential.builder()
                    .credentialId(new ByteArray(Base64.getDecoder().decode(credential.getCredentialId())))
                    .userHandle(new ByteArray(Base64.getDecoder().decode(credential.getUser().getHandle())))
                    .publicKeyCose(new ByteArray(Base64.getDecoder().decode(credential.getPublicKey())))
                    .signatureCount(credential.getCount())
                    .build()
        );
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        List<Authenticator> auth = authRepository.findAllByCredentialId(credentialId.getBase64());
        return auth.stream()
        .map(
            credential ->
                RegisteredCredential.builder()
                        .credentialId(new ByteArray(Base64.getDecoder().decode(credential.getCredentialId())))
                        .userHandle(new ByteArray(Base64.getDecoder().decode(credential.getUser().getHandle())))
                        .publicKeyCose(new ByteArray(Base64.getDecoder().decode(credential.getPublicKey())))
                        .signatureCount(credential.getCount())
                        .build())
        .collect(Collectors.toSet());
    }
}