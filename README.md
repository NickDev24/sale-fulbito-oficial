# Sale Fulbito

## Compilar APK Debug

```bash
export ANDROID_HOME=/home/facu/Android/Sdk
./gradlew assembleDebug
# APK → app/build/outputs/apk/debug/app-debug.apk
```

## Compilar APK Release (firmado)

```bash
export ANDROID_HOME=/home/facu/Android/Sdk
export KEYSTORE_PASSWORD=xxx
export KEY_ALIAS=upload
export KEY_PASSWORD=xxx
./gradlew assembleRelease
# APK → app/build/outputs/apk/release/app-release.apk
```

## Compilar AAB (Android App Bundle)

```bash
export ANDROID_HOME=/home/facu/Android/Sdk
export KEYSTORE_PASSWORD=xxx
export KEY_ALIAS=upload
export KEY_PASSWORD=xxx
./gradlew bundleRelease
# AAB → app/build/outputs/bundle/release/app-release.aab
```

### Keystore

El keystore debe estar en `my-upload-key.jks` en la raíz del proyecto, o configurar `KEYSTORE_PATH` con la ruta absoluta.

## Play Store Closed Testing

1. `./gradlew bundleRelease` → genera `app-release.aab`
2. Subir a Play Console > Producción > Crear nuevo lanzamiento
3. Configurar Closed Testing desde Play Console > Testing > Closed Testing

## Fingerprints de Firma (Firebase)

Agregar estas huellas SHA en Firebase Console > Configuración del proyecto > Huellas digitales.

| Build | Alias | SHA1 | SHA256 |
|-------|-------|------|--------|
| **Debug** | androiddebugkey | `23:70:0F:9C:A9:4D:B4:E6:CB:94:B5:3A:EF:84:92:13:C4:A6:57:8E` | `5C:DA:BA:59:F3:6D:AE:94:A4:A6:6D:78:A8:E7:FC:A3:43:BF:9A:C3:0C:8C:BF:A7:08:60:18:2B:75:29:29:F0` |
| **Release** | upload | `3A:1B:0D:7E:C9:13:EC:74:76:6F:68:5B:94:BB:73:9F:FA:37:FC:69` | `94:23:C3:56:C9:1F:CB:17:B0:3E:80:75:34:17:52:FA:4C:36:C9:E8:B5:86:8B:23:FC:CF:C4:FE:0D:58:E3:C8` |

## Cuentas de Prueba (Demo)

La app incluye 2 cuentas demo precargables desde la pantalla de inicio:

| Rol | Nombre | Email | Teléfono | Cómo cargar |
|-----|--------|-------|----------|-------------|
| **JUGADOR** | Juan Pérez | juan@test.com | 11 5555-0101 | Tap "CARGAR DATOS DE DEMO" |
| **DUEÑO** | Carlos García | carlos@test.com | 11 5555-0102 | Tap "CARGAR DATOS DUEÑO DE CANCHA" |

Para cambiar de rol: Perfil → "CERRAR SESIÓN" y cargar el otro demo.

## Variables de Entorno Requeridas

| Variable | Archivo |
|----------|---------|
| AdMob IDs | `.env` (ADMOB_BANNER_UNIT_ID, ADMOB_INTERSTITIAL_UNIT_ID, ADMOB_REWARDED_UNIT_ID) |
| Supabase URL/Key | `.env` (SUPABASE_URL, SUPABASE_ANON_KEY) |
| Keystore password | variable de entorno en shell |
