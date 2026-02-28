# JDTLS showing errors
1. Add this to every loader's `build.gradle` inside `dependencies`:
```
implementation project(':common')
```

2. Add this to root `build.gradle` inside `subprojects`:
```
apply plugin: 'eclipse'
```

3. Run:
```
./gradlew eclipse
```
*Optionaly add `--refresh-dependencies`*
