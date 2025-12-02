# Guide d'utilisation de Swagger/OpenAPI

## 📚 Documentation API

Votre projet inclut maintenant une documentation API complète via Swagger/OpenAPI.

### Accès à la documentation

Une fois le backend démarré, accédez à la documentation Swagger UI via :

**URL :** `http://localhost:8081/swagger-ui.html`

### Accès à l'API JSON (OpenAPI)

L'API JSON est disponible à :

**URL :** `http://localhost:8081/api-docs`

### Fonctionnalités

1. **Documentation interactive** : Testez directement les endpoints depuis l'interface Swagger
2. **Authentification JWT** : Cliquez sur le bouton "Authorize" en haut à droite pour ajouter votre token JWT
3. **Groupes d'endpoints** : Les endpoints sont organisés par tags (Étudiants, Classes, Authentification)
4. **Exemples de requêtes** : Chaque endpoint contient des exemples de requêtes et réponses

### Utilisation de l'authentification dans Swagger

1. Connectez-vous via `/api/auth/login` pour obtenir un token JWT
2. Cliquez sur le bouton **"Authorize"** (cadenas) en haut à droite
3. Entrez votre token JWT (sans le préfixe "Bearer ")
4. Cliquez sur "Authorize" puis "Close"
5. Tous les endpoints sécurisés seront maintenant accessibles

---

## 📄 Pagination et Tri

### Backend

Les endpoints suivants supportent maintenant la pagination et le tri :

- `GET /api/etudiants` - Liste tous les étudiants
- `GET /api/etudiants/classe/{classeId}` - Liste les étudiants d'une classe

### Paramètres de pagination

- `page` : Numéro de page (commence à 0) - **Par défaut : 0**
- `size` : Nombre d'éléments par page - **Par défaut : 10**
- `sortBy` : Champ de tri (nom, prenom, email) - **Par défaut : nom**
- `sortDir` : Direction du tri (asc, desc) - **Par défaut : asc**

### Exemple de requête

```
GET /api/etudiants/classe/1?page=0&size=10&sortBy=nom&sortDir=asc
```

### Réponse paginée

La réponse contient un objet `Page` avec :
- `content` : Liste des étudiants
- `totalElements` : Nombre total d'étudiants
- `totalPages` : Nombre total de pages
- `number` : Numéro de la page actuelle
- `size` : Taille de la page

### Frontend

L'interface utilisateur inclut maintenant :
- **Contrôles de pagination** : Boutons Précédent/Suivant
- **Sélecteur de taille de page** : 5, 10, 20, 50 éléments
- **Tri dynamique** : Cliquez sur les en-têtes de colonnes pour trier
- **Indicateur de page** : Affiche "Page X sur Y"

---

## ✅ Conformité au Guide du Projet

Votre projet répond maintenant aux exigences suivantes du guide :

1. ✅ **Documentation API** : Swagger/OpenAPI intégré
2. ✅ **Pagination** : Implémentée côté backend et frontend
3. ✅ **Tri dynamique** : Implémenté côté backend et frontend


