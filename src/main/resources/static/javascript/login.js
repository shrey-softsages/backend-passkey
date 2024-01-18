async function checkCredentials() {
    this.form = document.getElementById("form");
    const formData = new FormData(form);
    fetch('https://79ec-115-246-26-148.ngrok-free.app/login', {
        method: 'POST',
        body: formData
    })
    .then(response => initialCheckStatus(response))
    .then(function(credentialGetJson){
    console.log(credentialGetJson);
      return ({
        publicKey: {
        ...credentialGetJson.publicKey,
        allowCredentials: credentialGetJson.publicKey.allowCredentials
            && credentialGetJson.publicKey.allowCredentials.map(credential => ({
            ...credential,
            id: base64urlToUint8array(credential.id),
            })),
        challenge: base64urlToUint8array(credentialGetJson.publicKey.challenge),
        extensions: credentialGetJson.publicKey.extensions,
        },
    });
    }
    )
    .then(credentialGetOptions =>
        navigator.credentials.get(credentialGetOptions))
    .then(function(publicKeyCredential) {
    console.log(publicKeyCredential);
     return ({
        type: publicKeyCredential.type,
        id: publicKeyCredential.id,
        response: {
        authenticatorData: uint8arrayToBase64url(publicKeyCredential.response.authenticatorData),
        clientDataJSON: uint8arrayToBase64url(publicKeyCredential.response.clientDataJSON),
        signature: uint8arrayToBase64url(publicKeyCredential.response.signature),
        userHandle: publicKeyCredential.response.userHandle && uint8arrayToBase64url(publicKeyCredential.response.userHandle),
        },
        clientExtensionResults: publicKeyCredential.getClientExtensionResults(),
    });
    }
    )
    .then((encodedResult) => {
        document.getElementById("credential").value = JSON.stringify(encodedResult);
        console.log(document.getElementById("credential").value);
        this.form.submit();
    })
    .catch(error => displayError(error))
}