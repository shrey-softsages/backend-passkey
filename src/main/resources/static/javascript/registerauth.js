document.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    fetch('https://79ec-115-246-26-148.ngrok-free.app/register', {
        method: 'POST',
        body: formData
    })
    .then(response => initialCheckStatus(response))
    .then(function(credentialCreateJson) {
    console.log(credentialCreateJson);
    return ({
        publicKey: {
            ...credentialCreateJson.publicKey,
            challenge: base64urlToUint8array(credentialCreateJson.publicKey.challenge),
        user: {
            ...credentialCreateJson.publicKey.user,
            id: base64urlToUint8array(credentialCreateJson.publicKey.user.id),
        },
        excludeCredentials: credentialCreateJson.publicKey.excludeCredentials.map(credential => ({
            ...credential,
            id: base64urlToUint8array(credential.id),
        })),
        extensions: credentialCreateJson.publicKey.extensions,
        },
    })
    }
    )
    .then(credentialCreateOptions =>
        navigator.credentials.create(credentialCreateOptions))
    .then(function(publicKeyCredential) {
    console.log(publicKeyCredential);
     return ({

        type: publicKeyCredential.type,
        id: publicKeyCredential.id,
        response: {
        attestationObject: uint8arrayToBase64url(publicKeyCredential.response.attestationObject),
        clientDataJSON: uint8arrayToBase64url(publicKeyCredential.response.clientDataJSON),
        transports: publicKeyCredential.response.getTransports && publicKeyCredential.response.getTransports() || [],
        },
        clientExtensionResults: publicKeyCredential.getClientExtensionResults(),
    })

    }
    )
    .then((encodedResult) => {
        const form = document.getElementById("form");
        const formData = new FormData(form);
        formData.append("credential", JSON.stringify(encodedResult));
        console.log(encodedResult);
        return fetch("https://79ec-115-246-26-148.ngrok-free.app/finishauth", {
            method: 'POST',
            body: formData
        })
    })
    .then(function(response)  {
        followRedirect(response);
    })
    .catch((error) => {
        displayError(error);
    });
})