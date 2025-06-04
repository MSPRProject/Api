# Module API

Module API du projet Sanalyz.

## Prérequis

- Java 21
- Maven

## Configuration

Pour configurer l'API, il faut modifier le fichier src/resources/application.properties. La configuration de l'API IA sont obligatoires, mais elle n'a pas besoin d'être accessible, tant que les endpoints de l'IA ne sont pas utilisés.

## Lancement

Pour lancer l'API, il suffit de lancer la commande suivante dans le dossier du projet :

```bash
mvn spring-boot:run
```

La documentation de l'API est disponible sur l'endpoint : `/swagger-ui/index.html`
