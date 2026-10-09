# Porte coulissante SCP

ID : `scp_genesis:scp_sliding_door`. Onglet des blocs de construction.

Dimensions : 16 × 32 × 4 pixels. Plaque avant z=6..7, ossature avant z=7..8, ossature arrière z=8..9, plaque arrière z=9..10. Rails fixes de 0,5 pixel aux extrémités haute et basse. Les panneaux glissent de 16 pixels en 12 ticks (0,6 seconde), en sens opposés ; la droite est celle de la personne regardant la face avant.

Clic droit sur la porte fermée ou sur les rails de la porte ouverte. Commande redstone Vanilla également disponible. Les collisions passent à l'état ouvert/fermé dès la commande, comme les portes Vanilla. Les panneaux coulissants sont visuels dans les blocs voisins ; ils ne remplacent aucun bloc. Les murs opaques les masquent par le rendu de profondeur.

Une texture 512 × 512 : moitié gauche pour les plaques extérieures, moitié droite pour l'ossature et les rails. Générée avec l'outil ImageGen intégré, puis redimensionnée avec autorisation. Modèles JSON Vanilla modifiables dans Blockbench : models/block/scp_sliding_door_front.json et scp_sliding_door_back.json. Le modèle d'item regroupe la porte complète fermée.

Prompt de création : atlas carré partagé en deux ; à gauche panneau de porte métallique sombre, bordure fine rivetée, emblème blanc de la Fondation SCP en partie haute ; à droite acier sombre brossé uniforme pour les structures internes. Vue à plat sans perspective, surface opaque, contraste mesuré.
