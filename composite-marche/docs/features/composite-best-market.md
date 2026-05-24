# Spécification de fonctionnalité : Extensions de MarcheCompositeMeilleur

## 1. Vue d'ensemble

Ce document définit un ensemble d'extensions fonctionnelles indépendantes pour le composant `MarcheCompositeMeilleur` utilisé dans un système de routage de trading multi-marchés.

Le but de ce fichier est de servir de spécification lisible par machine pouvant être utilisée par des développeurs ou des assistants de codage basés sur des LLM.

---

## 2. Contexte du composant

### Composant de base

`MarcheCompositeMeilleur implémente Marche`

### Responsabilité principale

Router les ordres et les requêtes de marché à travers plusieurs instances sous-jacentes de `Marche` tout en sélectionnant les meilleures conditions d'exécution.

---

## 3. Extensions de fonctionnalités

### Fonctionnalité 1 : Départage par priorité des marchés

#### Description

Lorsque plusieurs marchés fournissent un prix d'exécution identique, la sélection doit être résolue à l'aide d'un système de priorité déterministe.

#### Entrées

* Liste de `Marche`
* Table de priorités : `Map<Marche, Integer>`

#### Règle

* La valeur entière de priorité la plus élevée l'emporte

#### Méthodes concernées

* `executerOrdre(Ordre)`

---

### Fonctionnalité 2 : Exécution partielle des ordres

#### Description

Les ordres peuvent être exécutés partiellement sur plusieurs marchés si aucun marché ne peut exécuter la quantité totale.

#### Comportement

* Répartir la quantité de l'ordre sur plusieurs marchés
* Agréger les résultats d'exécution en un seul résultat logique

#### Contraintes

* L'exécution doit suivre l'ordre du meilleur prix en premier

#### Méthodes concernées

* `executerOrdre(Ordre)`

---

### Fonctionnalité 3 : Stratégie de repli (fallback) d'exécution

#### Description

Si l'exécution échoue sur le meilleur marché sélectionné, le système doit réessayer sur le marché suivant le plus avantageux.

#### Comportement

* Maintenir une liste ordonnée de marchés candidats
* Réessayer séquentiellement en cas d'échec

#### Conditions d'échec

* Exception lors de l'exécution
* Ordre rejeté

#### Méthodes concernées

* `executerOrdre(Ordre)`

---

### Fonctionnalité 4 : Mise en cache des prix de marché

#### Description

La récupération des prix de marché doit être optimisée à l'aide d'un mécanisme de cache basé sur le temps.

#### Exigences

* Mettre en cache les résultats de `prixDuMarche(ticker)`
* TTL : 500 millisecondes

#### Comportement

* Retourner la valeur en cache si elle est encore valide
* Rafraîchir le cache lorsqu'il est expiré

#### Méthodes concernées

* `prixDuMarche(String ticker)`
* Couche de cache interne

---

### Fonctionnalité 5 : Support des ordres LIMIT

#### Description

Introduire des contraintes d'ordres LIMIT qui restreignent l'exécution en fonction de seuils de prix.

#### Règles

* LIMIT ACHAT : exécuter uniquement si le prix <= limite
* LIMIT VENTE : exécuter uniquement si le prix >= limite

#### Modifications requises

* Étendre le modèle `Ordre` avec un champ de contrainte de limite
* Modifier la logique de routage pour valider les contraintes avant sélection

#### Méthodes concernées

* `executerOrdre(Ordre)`