-- src/main/resources/data.sql
INSERT INTO animals (id, common_name, scientific_name, category, visibility_probability, short_description, location_description, map_url, photo_unlock_url, photo_lock_url) VALUES
(1, 'Lleó', 'Panthera leo', 'Mamífer', 'Baixa', 'El rei de la selva', 'Sabana africana', 'https://maps.example.com/lleo', '/images/lleo-unlock.jpg', '/images/lleo-lock.jpg'),
(2, 'Elefant', 'Loxodonta africana', 'Mamífer', 'Mitjana', 'L animal terrestre més gran', 'Sabana i boscos', 'https://maps.example.com/elefant', '/images/elefant-unlock.jpg', '/images/elefant-lock.jpg');