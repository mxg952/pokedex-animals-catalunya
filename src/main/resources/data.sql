-- Animals de Catalunya (20 animals)
INSERT INTO animals (id, common_name, scientific_name, category, visibility_probability, short_description, location_description, map_url, photo_unlock_url, photo_lock_url) VALUES
(1, 'Isard', 'Rupicapra pyrenaica', 'Mamífer', 'Mitjana', 'Cabra salvatge dels Pirineus', 'Pirineus catalans', 'https://maps.example.com/isard', '/images/isard-unlock.jpg', '/images/isard-lock.jpg'),
(2, 'Jabalí', 'Sus scrofa', 'Mamífer', 'Alta', 'Porc senglar comú als boscos', 'Boscos de tot Catalunya', 'https://maps.example.com/jabali', '/images/jabali-unlock.jpg', '/images/jabali-lock.jpg'),
(3, 'Dofí llistat', 'Stenella coeruleoalba', 'Marí', 'Mitjana', 'Dofí comú a la costa catalana', 'Costa Mediterrània', 'https://maps.example.com/dofi-llistat', '/images/dofi-llistat-unlock.jpg', '/images/dofi-llistat-lock.jpg'),
(4, 'Tortuga mediterrània', 'Testudo hermanni', 'Rèptil', 'Baixa', 'Tortuga terrestre autòctona', 'Muntanyes litorals', 'https://maps.example.com/tortuga-mediterrania', '/images/tortuga-med-unlock.jpg', '/images/tortuga-med-lock.jpg'),
(5, 'Àliga cuabarrada', 'Hieraaetus fasciatus', 'Ocell', 'Baixa', 'Rapinyaire de gran mida', 'Serralades Prelitorals', 'https://maps.example.com/aliga-cuabarrada', '/images/aliga-unlock.jpg', '/images/aliga-lock.jpg'),
(6, 'Tortuga d''estany', 'Emys orbicularis', 'Rèptil', 'Mitjana', 'Tortuga aquàtica autòctona', 'Aiguamolls i estanys', 'https://maps.example.com/tortuga-estany', '/images/tortuga-estany-unlock.jpg', '/images/tortuga-estany-lock.jpg'),
(7, 'Granota verda', 'Pelophylax perezi', 'Amfibi', 'Alta', 'Granota comuna en zones humides', 'Aiguamolls i rius', 'https://maps.example.com/granota-verda', '/images/granota-unlock.jpg', '/images/granota-lock.jpg'),
(8, 'Cadernera', 'Carduelis carduelis', 'Ocell', 'Alta', 'Ocell cantaire de colors vius', 'Zones agrícoles i jardins', 'https://maps.example.com/cadernera', '/images/cadernera-unlock.jpg', '/images/cadernera-lock.jpg'),
(9, 'Fagina', 'Genetta genetta', 'Mamífer', 'Baixa', 'Mamífer carnívor nocturn', 'Boscos mediterranis', 'https://maps.example.com/fagina', '/images/fagina-unlock.jpg', '/images/fagina-lock.jpg'),
(10, 'Serp verda', 'Malpolon monspessulanus', 'Rèptil', 'Mitjana', 'Serp llarga i àgil', 'Garrigues i zones seques', 'https://maps.example.com/serp-verda', '/images/serp-verda-unlock.jpg', '/images/serp-verda-lock.jpg'),
(11, 'Bernat pescaire', 'Ardea cinerea', 'Ocell', 'Alta', 'Ocell aquàtic de gran mida', 'Deltes i aiguamolls', 'https://maps.example.com/bernat-pescaire', '/images/bernat-unlock.jpg', '/images/bernat-lock.jpg'),
(12, 'Eriçó', 'Erinaceus europaeus', 'Mamífer', 'Mitjana', 'Mamífer insectívor amb espines', 'Boscos i zones rurals', 'https://maps.example.com/erico', '/images/erico-unlock.jpg', '/images/erico-lock.jpg'),
(13, 'Llagost', 'Locusta migratoria', 'Insecte', 'Alta', 'Insecte saltador comú', 'Prats i zones obertes', 'https://maps.example.com/llagost', '/images/llagost-unlock.jpg', '/images/llagost-lock.jpg'),
(14, 'Fartet', 'Aphanius iberus', 'Peix', 'Baixa', 'Peix autòcton en perill', 'Aiguamolls costaners', 'https://maps.example.com/fartet', '/images/fartet-unlock.jpg', '/images/fartet-lock.jpg'),
(15, 'Ratpenat de ferradura', 'Rhinolophus ferrumequinum', 'Mamífer', 'Baixa', 'Ratpenat insectívor', 'Coves i edificis antics', 'https://maps.example.com/ratpenat-ferradura', '/images/ratpenat-unlock.jpg', '/images/ratpenat-lock.jpg'),
(16, 'Cargol', 'Helix aspersa', 'Mollusc', 'Alta', 'Cargol terrestre comú', 'Jardins i camps', 'https://maps.example.com/cargol', '/images/cargol-unlock.jpg', '/images/cargol-lock.jpg'),
(17, 'Esparver', 'Accipiter nisus', 'Ocell', 'Mitjana', 'Rapinyaire forestal', 'Boscos i zones arbolades', 'https://maps.example.com/esparver', '/images/esparver-unlock.jpg', '/images/esparver-lock.jpg'),
(18, 'Salamandra', 'Salamandra salamandra', 'Amfibi', 'Mitjana', 'Amfibi negre i grog', 'Rius i zones humides forestals', 'https://maps.example.com/salamandra', '/images/salamandra-unlock.jpg', '/images/salamandra-lock.jpg'),
(19, 'Gamba', 'Palaemon serratus', 'Crustaci', 'Alta', 'Crustaci decàpode marí', 'Costes rocoses i praderies', 'https://maps.example.com/gamba', '/images/gamba-unlock.jpg', '/images/gamba-lock.jpg'),
(20, 'Puput', 'Upupa epops', 'Ocell', 'Mitjana', 'Ocell amb cresta característica', 'Campos oberts i zones rurals', 'https://maps.example.com/puput', '/images/puput-unlock.jpg', '/images/puput-lock.jpg');

-- Mesos d'observació per als animals de Catalunya
