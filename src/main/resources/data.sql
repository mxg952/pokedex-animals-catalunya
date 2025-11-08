-- Animals d'exemple
INSERT INTO animals (id, common_name, scientific_name, category, visibility_probability, short_description, location_description, map_url, photo_unlock_url, photo_lock_url) VALUES
(1, 'Lleó', 'Panthera leo', 'Mamífer', 'Baixa', 'El rei de la selva', 'Sabana africana', 'https://maps.example.com/lleo', '/images/lleo-unlock.jpg', '/images/lleo-lock.jpg'),
(2, 'Elefant', 'Loxodonta africana', 'Mamífer', 'Mitjana', 'L animal terrestre més gran', 'Sabana i boscos', 'https://maps.example.com/elefant', '/images/elefant-unlock.jpg', '/images/elefant-lock.jpg'),
(3, 'Dofí', 'Delphinus delphis', 'Marí', 'Alta', 'Intel·ligent i amigable', 'Oceans i mars', 'https://maps.example.com/dofi', '/images/dofi-unlock.jpg', '/images/dofi-lock.jpg');


-- Crear la taula si no existeix
CREATE TABLE IF NOT EXISTS animal_sighting_months (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    animal_id BIGINT,
    month VARCHAR(50),
    FOREIGN KEY (animal_id) REFERENCES animals(id)
);

-- Després inserir les dades
INSERT INTO animal_sighting_months (animal_id, month) VALUES
(1, 'Gener'), (1, 'Febrer'), (1, 'Març'),
(2, 'Abril'), (2, 'Maig'), (2, 'Juny'),
(3, 'Juliol'), (3, 'Agost'), (3, 'Setembre');