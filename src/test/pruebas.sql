-- Test 1
SELECT nombre, precio FROM producto;

-- Test 2
SELECT *, precio * 1.09 AS precio_en_dolares FROM producto;

-- Test 3
SELECT UPPER(nombre) AS nombre_mayuscula, precio FROM producto;

-- Test 4
SELECT nombre, UPPER(SUBSTRING(nombre, 1, 2)) AS primeros_caracteres FROM fabricante;

-- Test 5
SELECT DISTINCT codigo FROM fabricante
WHERE codigo IN (SELECT DISTINCT codigo_fabricante FROM producto);

-- Test 6
SELECT nombre FROM fabricante
ORDER BY nombre DESC;

-- Test 7
SELECT nombre, precio FROM producto
ORDER BY nombre ASC, precio DESC;

-- Test 8
SELECT * FROM fabricante
                  LIMIT 5;

-- Test 9
SELECT * FROM fabricante
                  LIMIT 2 OFFSET 3;

-- Test 10
SELECT nombre, precio FROM producto
ORDER BY precio ASC LIMIT 1;

-- Test 11
SELECT nombre, precio FROM producto
ORDER BY precio DESC LIMIT 1;

-- Test 12
SELECT nombre FROM producto
WHERE codigo_fabricante = 2;

-- Test 13
SELECT nombre FROM producto
WHERE precio <= 120;

-- Test 14
SELECT * FROM producto
WHERE precio >= 400;

-- Test 15
SELECT * FROM producto
WHERE precio BETWEEN 80 AND 300;

-- Test 16
SELECT * FROM producto
WHERE precio > 200 AND codigo_fabricante = 6;

-- Test 17
SELECT * FROM producto
WHERE producto.codigo_fabricante IN (1, 3, 5);

-- Test 18
SELECT nombre, precio * 100 AS precio_en_centimos FROM producto;

-- Test 19
SELECT nombre FROM fabricante
WHERE nombre LIKE 'S%';

-- Test 20
SELECT * FROM producto
WHERE nombre LIKE '%Portátil%';

-- Test 21
SELECT nombre FROM producto
WHERE nombre LIKE '%Monitor%' AND precio < 215;

-- Test 22
SELECT nombre, precio FROM producto
WHERE precio >= 180
ORDER BY precio DESC, nombre ASC;


-- Test 23
SELECT producto.nombre, precio, fabricante.nombre
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
ORDER BY fabricante.nombre;

-- Test 24
SELECT producto.nombre, precio, fabricante.nombre
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
ORDER BY precio DESC
    LIMIT 1;

-- Test 25
SELECT producto.nombre, precio
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE fabricante.nombre = 'Crucial' AND precio > 200;

-- Test 26
SELECT producto.nombre, precio, fabricante.nombre
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE fabricante.nombre IN ('Asus', 'Hewlett-Packard', 'Seagate');

-- Test 27
SELECT producto.nombre, precio, fabricante.nombre
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE precio >= 180
ORDER BY precio DESC, producto.nombre ASC;

-- Test 28
SELECT fabricante.nombre, producto.nombre
FROM fabricante
         LEFT JOIN producto ON fabricante.codigo = producto.codigo_fabricante;

-- Test 29
SELECT nombre
FROM fabricante
WHERE codigo NOT IN (SELECT DISTINCT codigo_fabricante FROM producto);

-- Test 30
SELECT COUNT(*)
FROM producto;

-- Test 31
SELECT COUNT(DISTINCT codigo_fabricante)
FROM producto;

-- Test 32
SELECT AVG(precio)
FROM producto;

-- Test 33
SELECT MIN(precio)
FROM producto;

-- Test 34
SELECT SUM(precio)
FROM producto;

-- Test 35
SELECT COUNT(*)
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE fabricante.nombre = 'Asus';

-- Test 36
SELECT AVG(precio)
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE fabricante.nombre = 'Asus';

-- Test 37
SELECT MIN(precio), MAX(precio), AVG(precio), COUNT(*)
FROM producto
         JOIN fabricante ON producto.codigo_fabricante = fabricante.codigo
WHERE fabricante.nombre = 'Crucial';

-- Test 38
SELECT fabricante.nombre, COUNT(producto.codigo) AS num_productos
FROM fabricante
         LEFT JOIN producto ON fabricante.codigo = producto.codigo_fabricante
GROUP BY fabricante.nombre
ORDER BY num_productos DESC;

-- Test 39
SELECT fabricante.nombre,
       MIN(producto.precio) AS precio_minimo,
       MAX(producto.precio) AS precio_maximo,
       AVG(producto.precio) AS precio_medio
FROM fabricante
         LEFT JOIN producto ON fabricante.codigo = producto.codigo_fabricante
GROUP BY fabricante.nombre;

-- Test 40
SELECT fabricante.codigo,
       MIN(producto.precio) AS precio_minimo,
       MAX(producto.precio) AS precio_maximo,
       AVG(producto.precio) AS precio_medio,
       COUNT(producto.codigo) AS num_productos
FROM fabricante
         JOIN producto ON fabricante.codigo = producto.codigo_fabricante
GROUP BY fabricante.codigo
HAVING AVG(producto.precio) > 200;

-- Test 41
SELECT nombre
FROM fabricante
WHERE codigo IN (
    SELECT codigo_fabricante
    FROM producto
    GROUP BY codigo_fabricante
    HAVING COUNT(*) >= 2
);

-- Test 42
SELECT fabricante.nombre, COUNT(producto.codigo) AS num_productos
FROM fabricante
         JOIN producto ON fabricante.codigo = producto.codigo_fabricante
WHERE producto.precio >= 220
GROUP BY fabricante.nombre
ORDER BY num_productos DESC;

-- Test 43
SELECT nombre
FROM fabricante
WHERE codigo IN (
    SELECT codigo_fabricante
    FROM producto
    GROUP BY codigo_fabricante
    HAVING SUM(precio) > 1000
);

-- Test 44
SELECT fabricante.nombre, SUM(producto.precio) AS total_precio
FROM fabricante
         JOIN producto ON fabricante.codigo = producto.codigo_fabricante
GROUP BY fabricante.nombre
HAVING SUM(producto.precio) > 1000
ORDER BY total_precio ASC;

-- Test 45
SELECT fabricante.nombre, producto.nombre, MAX(producto.precio) AS precio
FROM fabricante
         JOIN producto ON fabricante.codigo = producto.codigo_fabricante
group by fabricante.nombre, producto.nombre ;

-- Test 46
SELECT producto.nombre, producto.precio, fabricante.nombre
FROM fabricante
         JOIN producto ON fabricante.codigo = producto.codigo_fabricante
WHERE producto.precio >= (
    SELECT AVG(p.precio)
    FROM producto p
    WHERE p.codigo_fabricante = fabricante.codigo
)
ORDER BY fabricante.nombre ASC, producto.precio DESC;