# LS Motors - Admin Client (Java Swing)

## Objectif
Client lourd (Java) pour gérer la partie admin :
- CRUD Utilisateurs (tous rôles)
- CRUD Catégories
- CRUD Marques
- CRUD Véhicules
- Historique des ventes par semaine (global + par employé)
- Config du % de commission (valeur clé `commission_pct`)

## Prérequis
- Java 17
- MySQL/MariaDB accessible (même BDD que ton projet web)
- Maven (ou import Maven dans IntelliJ/Eclipse)

## Lancer
1. Configure `src/main/resources/config.properties`
2. Dans un terminal :
   - `mvn clean package`
   - `java -jar target/lsmotors-admin-client-1.0.0.jar`

## IMPORTANT (adaptation schéma)
Je n'ai pas ton SQL exact LS Motors ici.
Le fichier `config.properties` contient les noms de tables/colonnes attendus.
Si ta BDD a des noms différents, tu modifies juste ce fichier et ça repart.

## Table config (si pas déjà)
```sql
CREATE TABLE IF NOT EXISTS config (
  cle VARCHAR(50) PRIMARY KEY,
  valeur VARCHAR(50) NOT NULL
);
INSERT IGNORE INTO config(cle, valeur) VALUES('commission_pct', '40');
```
