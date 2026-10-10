# Readers et configurateur

## Utilisation

- Un seul bloc/item : `scp_genesis:reader`, à poser sur une face verticale solide.
- Outil : `scp_genesis:reader_configurator`. Tous deux figurent dans l'onglet Portes et accès.
- Clic droit avec l'outil : ouvrir la configuration. Les réglages sont appliqués par Valider/Enregistrer.
- Chaque slot accepte une porte coulissante SCP dans la même dimension. Saisir X/Y/Z, ou cliquer sur Lier puis sur la porte. Les coordonnées enregistrées désignent toujours sa moitié basse.
- Les trois champs vides retirent le lien. Maj + clic droit dans le vide annule une sélection en cours. La sélection expire après deux minutes ; rester à moins de 64 blocs du lecteur pour ce parcours.
- Un identifiant déjà employé active l'avertissement et Copier. La confirmation copie une configuration existante, y compris ses liens et son code. Ce sont ensuite des configurations indépendantes, pas une synchronisation permanente.
- ID est réservé et désactivé. Keycard exige une carte dont le niveau est supérieur ou égal au seuil 0–6. Code utilise un code numérique de 1 à 12 chiffres, avec clavier, validation et effacement. Bouton accepte une interaction normale. Locked refuse tout accès ; seul le configurateur permet de changer son type pour le déverrouiller.
- Les cartes O5 sont classées niveau 6. Les départements ne participent pas au contrôle d'accès. Les tags `scp_genesis:keycards/level_0` à `level_6` permettent de compléter ces associations par datapack.

## Durées et portes

Ouverture acceptée : Opening pendant 200 ticks, puis Closing pendant 100 ticks, puis Base. La porte se ferme au début de Closing. Refus d'accréditation : Error pendant 50 ticks. Mauvais code : Error pendant 100 ticks. Les tentatives pendant un cycle ne prolongent pas ses délais.

Les boutons Ouvrir/Fermer du configurateur pilotent les deux liens enregistrés. Une porte associée à un lecteur n'accepte plus l'ouverture directe au clic ou par redstone, ce qui empêcherait autrement Locked et les codes de contrôler son accès. Après suppression de tous ses liens, son fonctionnement manuel initial revient. Plusieurs lecteurs peuvent piloter la même porte : une demande d'ouverture encore valide la maintient ouverte.

Les coordonnées nouvellement saisies doivent désigner une porte chargée. Une porte déjà liée dans un chunk déchargé respecte la demande restante lorsqu'elle est rechargée. Les échéances absolues sauvegardées empêchent une porte de rester ouverte indéfiniment après déchargement de son lecteur.

## Organisation

Le comportement, la sauvegarde, les écrans et les paquets utilisent uniquement les classes Vanilla dans `common/scps/readers`. Les cinq stratégies sont des classes séparées, sans multiplier les blocs ou items enregistrés. Les adaptateurs réseau et l'enregistrement de l'écran sont dans `fabric/scps/readers` et `neoforge/scps/readers`. L'envoi privé passe par `PlatformServices`.

Les codes ne sont pas envoyés aux joueurs qui ouvrent le clavier ordinaire ni dans les mises à jour publiques de block entities. Chaque commande réseau est vérifiée contre le menu serveur courant, la distance, le type du lecteur et la possession du configurateur pour les modifications.

Les modèles restent ceux du projet. Les 164 règles multipart couvrent exactement les 640 combinaisons des propriétés facing/type/level/status, y compris les états non canoniques, sans multiplier les géométries.
