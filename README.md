# Autenticación por Biometricos

1. Agregar la dependencia en el archivo `app/build.gradle` 

```java
dependencies {
    implementation 'androidx.biometric:biometric:1.0.0-beta01'
}
```
https://developer.android.com/reference/androidx/biometric/BiometricPrompt.html

3. Utilizaremos las siguientes clases para crear un dialogo de autenticación
`BiometricPrompt` la cual tiene el siguiente constructor

```java
public BiometricPrompt (FragmentActivity fragmentActivity, 
                Executor executor,   
                BiometricPrompt.AuthenticationCallback callback)
```

`FragmentActivity`   el FragmentActivity se puede interpretar como una subactividad del main activity donde se mostrara el cuadro de dialogo de autenticación y estará a la escucha de eventos de entrada.
`Executor` necesitamos este objeto para manejar eventos callback.
`BiometricPrompt.AuthenticationCallback` Es una clase abstracta que se utiliza para escuchar los eventos biometricos, 
esta clase cuenta con 3 métodos abstractos:

- `onAuthenticationError()`  se activa cuando presionamos el botón de cancelar en el dialogo de autenticación
- `onAuthenticationSucceeded()` se activa cuando nos autenticamos de manera satisfactoria
-  `onAuthenticationFailed()` se activa cuando la autenticación es fallida



**Nota: ** dependiendo de la configuración de biometricos guardado por el usuario la aplicación decidirá si utilizará la autenticación mediante iris o huella dactilar.

A continuación se muestra la implementación de la clase abstracta **BiometricPrompt.AuthenticationCallback**

```java
final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {  
    @Override  
  public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {  
        super.onAuthenticationError(errorCode, errString);  
        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {  
            Log.d("EVENTO", "He Cancelado");  
        } else {  
            //TODO: Called when an unrecoverable error has been encountered and the operation is complete.  
  }  
    }  
  
    @Override  
  public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {  
        super.onAuthenticationSucceeded(result);  
        Log.d("EVENTO", "He Accedido");  
        //TODO: Called when a biometric is recognized.  
  }  
  
    @Override  
  public void onAuthenticationFailed() {  
        super.onAuthenticationFailed();  
        //TODO: Called when a biometric is valid but not recognized.  
  Log.d("EVENTO", "Huella no reconocida");  
    }  
});
```

Para personalizar nuestro dialogo de autenticación haremos uso de un constructor que utiliza el patrón de diseño
Builder.

```java
final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()  
        .setTitle("Set the title to display.")  
        .setSubtitle("Set the subtitle to display.")  
        .setDescription("Set the description to display")  
        .setNegativeButtonText("Negative Button")  
        .build();
```

Para poder activar el cuadro de dialogo de autenticación usaremos la siguiente linea

```java
biometricPrompt.authenticate(promptInfo);
```

# Encryption

1. Encriptar mensaje 
```
byte[] plaintext = ...;
KeyGenerator keygen = KeyGenerator.getInstance("AES");
keygen.init(256);
SecretKey key = keygen.generateKey();
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] ciphertext = cipher.doFinal(plaintext);
byte[] iv = cipher.getIV();
```
2. Desencriptar mensaje
```
byte[] plaintext = ...;
KeyGenerator keygen = KeyGenerator.getInstance("AES");
keygen.init(256);
SecretKey key = keygen.generateKey();
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] ciphertext = cipher.doFinal(plaintext);
byte[] iv = cipher.getIV();
```
3. Generar llaves
```
MessageDigest sha = MessageDigest.getInstance("SHA-256");
byte[] key = password.getBytes("UTF-8");
key = sha.digest(key);
SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
```
# Tutorial

https://www.youtube.com/watch?v=kN8hlHO8UZ1

