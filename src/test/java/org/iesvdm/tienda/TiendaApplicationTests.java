package org.iesvdm.tienda;

import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Comparator.comparing;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;

	@Autowired
	ProductoRepository prodRepo;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();

		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}

	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});

	}

	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();
		var lista = listProds.stream().map(producto -> producto.getNombre()+" "+producto.getPrecio()).toList();
		Assertions.assertEquals(11, lista.size());
	}

	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
	 */
	@Test
	void test2() {
		var listProds = prodRepo.findAll();
		var sumaProductos = listProds.stream().map(Producto::getPrecio).reduce(Double::sum).orElse(null);
		listProds.forEach(producto -> producto.setPrecio(producto.getPrecio()*1.09));
		var sumaFinal = listProds.stream().map(Producto::getPrecio).reduce(Double::sum).orElse(null);
		Assertions.assertEquals(sumaFinal, sumaProductos*1.09);
	}

	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();
		var list = listProds.stream().map(producto -> producto.getNombre().toUpperCase()+" "+producto.getPrecio()).toList();
		Assertions.assertTrue(list.getFirst().contains("DISCO DURO SATA3 1TB"));
	}

	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();
		var list = listFabs.stream().map(fabricante -> fabricante.getNombre()+","+fabricante.getNombre().substring(0,2).toUpperCase()).toList();

		Assertions.assertEquals("Asus,AS",list.getFirst());
	}

	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();
		var list = listFabs.stream().filter(fabricante -> !fabricante.getProductos().isEmpty()).map(Fabricante::getCodigo).distinct().toList();
		Assertions.assertEquals(7, list.size());
		System.out.println(list);
	}

	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();
		var list = listFabs.stream().map(Fabricante::getNombre).sorted(Comparator.reverseOrder()).toList();
		Assertions.assertEquals("Xiaomi",list.getFirst());
	}

	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();
		var list = listProds.stream()
				.sorted(comparing(Producto::getNombre)
						.thenComparing(comparing(Producto::getPrecio).reversed()))
				.map(p -> p.getNombre() + " " + p.getPrecio())
				.toList();
		Assertions.assertFalse(list.isEmpty());
		System.out.println(list);
	}

	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();
		var list = listFabs.stream().limit(5).toList();
		Assertions.assertEquals(5, list.size());
		System.out.println(list);
	}

	/**
	 * 9. Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();
		var list = listFabs.stream().skip(3).limit(2).toList();
		Assertions.assertEquals(2, list.size());
		System.out.println(list);
	}

	/**
	 * 10. Lista el nombre y el precio del producto más barato.
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();
		var producto = listProds.stream().min(comparing(Producto::getPrecio)).orElse(null);
		Assertions.assertNotNull(producto);
		Assertions.assertEquals("Impresora HP Deskjet 3720", producto.getNombre());
		System.out.println(producto.getNombre() + " " + producto.getPrecio());
	}

	/**
	 * 11. Lista el nombre y el precio del producto más caro.
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
		var producto = listProds.stream().max(comparing(Producto::getPrecio)).orElse(null);
		Assertions.assertNotNull(producto);
		Assertions.assertEquals("GeForce GTX 1080 Xtreme", producto.getNombre());
		System.out.println(producto.getNombre() + " " + producto.getPrecio());
	}

	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getFabricante().getCodigo() == 2)
				.map(Producto::getNombre)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(2, productos.size());
		System.out.println(productos);
	}

	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() <= 120)
				.map(Producto::getNombre)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(3, productos.size());
		System.out.println(productos);
	}

	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() >= 400)
				.map(Producto::getNombre)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(3, productos.size());
		System.out.println(productos);
	}

	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€.
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(7, productos.size());
		System.out.println(productos);
	}

	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() > 200 && p.getFabricante().getCodigo() == 6)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals("GeForce GTX 1080 Xtreme", productos.getFirst().getNombre());
		System.out.println(productos);
	}

	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de códigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();
		var codigos = Set.of(1, 3, 5);
		var productos = listProds.stream()
				.filter(p -> codigos.contains(p.getFabricante().getCodigo()))
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(5, productos.size());
		System.out.println(productos);
	}

	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.map(p -> p.getNombre() + " " + (int) (p.getPrecio() * 100))
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertTrue(productos.getFirst().contains("8699"));
		System.out.println(productos);
	}

	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S.
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();
		var fabricantes = listFabs.stream()
				.map(Fabricante::getNombre)
				.filter(s -> s.startsWith("S"))
				.toList();
		Assertions.assertFalse(fabricantes.isEmpty());
		Assertions.assertEquals(2, fabricantes.size());
		System.out.println(fabricantes);
	}

	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena "Portátil" en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getNombre().contains("Portátil"))
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(2, productos.size());
		System.out.println(productos);
	}

	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena "Monitor" en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getNombre().contains("Monitor") && p.getPrecio() < 215)
				.map(Producto::getNombre)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(1, productos.size());
		System.out.println(productos);
	}

	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€.
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
	@Test
	void test22() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() >= 180)
				.sorted(comparing(Producto::getPrecio).reversed()
						.thenComparing(Producto::getNombre))
				.map(p -> p.getNombre() + " " + p.getPrecio())
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertTrue(productos.getFirst().contains("GeForce GTX 1080 Xtreme"));
		System.out.println(productos);
	}

	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.sorted(comparing(p -> p.getFabricante().getNombre()))
				.map(p -> p.getNombre() + " " + p.getPrecio() + " " + p.getFabricante().getNombre())
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertTrue(productos.getFirst().contains("Asus"));
		System.out.println(productos);
	}

	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();
		var producto = listProds.stream()
				.max(comparing(Producto::getPrecio))
				.map(p -> p.getNombre() + " " + p.getPrecio() + " " + p.getFabricante().getNombre())
				.orElse(null);
		Assertions.assertNotNull(producto);
		System.out.println(producto);
	}

	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Crucial") && p.getPrecio() > 200)
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals("GeForce GTX 1080 Xtreme",productos.getFirst().getNombre());
		System.out.println(productos);
	}

	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate.
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();
		var fabricantes = Set.of("Asus", "Hewlett-Packard", "Seagate");
		var productos = listProds.stream()
				.filter(p -> fabricantes.contains(p.getFabricante().getNombre()))
				.toList();
		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(5, productos.size());
		System.out.println(productos);
	}


	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€.
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

	 Producto                Precio             Fabricante
	 -----------------------------------------------------
	 GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
	 Portátil Yoga 520      |452.79            |Lenovo
	 Portátil Ideapd 320    |359.64000000000004|Lenovo
	 Monitor 27 LED Full HD |199.25190000000003|Asus

	 */
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
		var productos = listProds.stream()
				.filter(p -> p.getPrecio() >= 180)
				.sorted(comparing(Producto::getPrecio).reversed()
						.thenComparing(Producto::getNombre))
				.toList();

		System.out.println("Producto                Precio             Fabricante");
		System.out.println("-----------------------------------------------------");
		productos.forEach(p -> {
			String producto = String.format("%-20s", p.getNombre());
			String precio = String.format("%-20.2f", p.getPrecio());
			String fabricante = p.getFabricante().getNombre();
			System.out.println(producto + "|" + precio + "|" + fabricante);
		});

		Assertions.assertFalse(productos.isEmpty());
		Assertions.assertEquals(7, productos.size());
	}


	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos.
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
	 Fabricante: Asus

	 Productos:
	 Monitor 27 LED Full HD
	 Monitor 24 LED Full HD

	 Fabricante: Lenovo

	 Productos:
	 Portátil Ideapd 320
	 Portátil Yoga 520

	 Fabricante: Hewlett-Packard

	 Productos:
	 Impresora HP Deskjet 3720
	 Impresora HP Laserjet Pro M26nw

	 Fabricante: Samsung

	 Productos:
	 Disco SSD 1 TB

	 Fabricante: Seagate

	 Productos:
	 Disco duro SATA3 1TB

	 Fabricante: Crucial

	 Productos:
	 GeForce GTX 1080 Xtreme
	 Memoria RAM DDR4 8GB

	 Fabricante: Gigabyte

	 Productos:
	 GeForce GTX 1050Ti

	 Fabricante: Huawei

	 Productos:


	 Fabricante: Xiaomi

	 Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
		listFabs.stream().map(fabricante ->
				"Fabricante: "+fabricante.getNombre()+"\n\n"
						+"Productos:"+"\n"
						+fabricante.getProductos().stream().map(producto -> producto.getNombre()+"\n").collect(Collectors.joining())
		).forEach(System.out::println);

	}

	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();
		var fabricantesSinProductos = listFabs.stream()
				.filter(f -> f.getProductos().isEmpty())
				.map(Fabricante::getNombre)
				.toList();
		Assertions.assertFalse(fabricantesSinProductos.isEmpty());
		Assertions.assertEquals(2, fabricantesSinProductos.size());
		System.out.println(fabricantesSinProductos);
	}

	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		long totalProductos = listProds.stream().count();
		Assertions.assertTrue(totalProductos > 0);
		System.out.println("Número total de productos: " + totalProductos);
	}

	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();
		long fabricantesConProductos = listProds.stream()
				.map(Producto::getFabricante)
				.distinct()
				.count();
		Assertions.assertTrue(fabricantesConProductos > 0);
		System.out.println("Número de fabricantes con productos: " + fabricantesConProductos);
	}

	/**
	 * 32. Calcula la media del precio de todos los productos.
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();
		double mediaPrecio = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.average()
				.orElse(0.0);
		Assertions.assertTrue(mediaPrecio > 0);
		System.out.println("Media del precio de todos los productos: " + mediaPrecio);
	}

	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		double precioMasBarato = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.min()
				.orElse(0.0);
		Assertions.assertTrue(precioMasBarato > 0);
		System.out.println("Precio más barato: " + precioMasBarato);
	}

	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		double sumaPrecios = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.sum();
		Assertions.assertTrue(sumaPrecios > 0);
		System.out.println("Suma de los precios de todos los productos: " + sumaPrecios);
	}

	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		long productosAsus = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Asus"))
				.count();
		Assertions.assertTrue(productosAsus > 0);
		System.out.println("Número de productos del fabricante Asus: " + productosAsus);
	}

	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		double mediaPrecioAsus = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Asus"))
				.mapToDouble(Producto::getPrecio)
				.average()
				.orElse(0.0);
		Assertions.assertTrue(mediaPrecioAsus > 0);
		System.out.println("Media del precio de productos Asus: " + mediaPrecioAsus);
	}

	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial.
	 * Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();
		Double[] result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Crucial"))
				.mapToDouble(Producto::getPrecio)
				.collect(() -> new Double[]{Double.MAX_VALUE, Double.MIN_VALUE, 0.0, 0.0},
						(acc, precio) -> {
							acc[0] = Math.min(acc[0], precio);
							acc[1] = Math.max(acc[1], precio);
							acc[2] += precio;
							acc[3]++;
						},
						(acc1, acc2) -> {
							acc1[0] = Math.min(acc1[0], acc2[0]);
							acc1[1] = Math.max(acc1[1], acc2[1]);
							acc1[2] += acc2[2];
							acc1[3] += acc2[3];
						});

		double precioMedio = result[3] > 0 ? result[2] / result[3] : 0.0;
		System.out.println("Precio mínimo: " + result[0]);
		System.out.println("Precio máximo: " + result[1]);
		System.out.println("Precio medio: " + precioMedio);
		System.out.println("Número total de productos: " + result[3]);

		Assertions.assertTrue(result[3] > 0);
	}


	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes.
	 * El listado también debe incluir los fabricantes que no tienen ningún producto.
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene.
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:

	 Fabricante     #Productos
	 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	 Asus              2
	 Lenovo              2
	 Hewlett-Packard              2
	 Samsung              1
	 Seagate              1
	 Crucial              2
	 Gigabyte              1
	 Huawei              0
	 Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		var resultado = listFabs.stream()
				.sorted((fab1, fab2) -> Integer.compare(
						fab2.getProductos().size(), fab1.getProductos().size()))
				.map(fab -> String.format("%-20s %d", fab.getNombre(), fab.getProductos().size()))
				.toList();

		resultado.forEach(System.out::println); // Imprime el resultado para comprobar
		Assertions.assertFalse(resultado.isEmpty());
	}

	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes.
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		listFabs.forEach(fab -> {
			Double[] stats = fab.getProductos().stream()
					.mapToDouble(Producto::getPrecio)
					.collect(() -> new Double[]{Double.MAX_VALUE, Double.MIN_VALUE, 0.0, 0.0},
							(acc, precio) -> {
								acc[0] = Math.min(acc[0], precio);
								acc[1] = Math.max(acc[1], precio);
								acc[2] += precio;
								acc[3]++;
							},
							(acc1, acc2) -> {
								acc1[0] = Math.min(acc1[0], acc2[0]);
								acc1[1] = Math.max(acc1[1], acc2[1]);
								acc1[2] += acc2[2];
								acc1[3] += acc2[3];
							});
			double precioMedio = stats[3] > 0 ? stats[2] / stats[3] : 0.0;
			System.out.println("Fabricante: " + fab.getNombre() + " | Mínimo: " + stats[0] + " | Máximo: " + stats[1] + " | Medio: " + precioMedio);
		});
	}

	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€.
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		listFabs.stream()
				.filter(fab -> fab.getProductos().stream()
						.mapToDouble(Producto::getPrecio)
						.average()
						.orElse(0.0) > 200.0)
				.forEach(fab -> {
					DoubleSummaryStatistics stats = fab.getProductos().stream()
							.mapToDouble(Producto::getPrecio)
							.summaryStatistics();
					System.out.println("Fabricante código: " + fab.getCodigo() + " | Mínimo: " + stats.getMin() + " | Máximo: " + stats.getMax() + " | Medio: " + stats.getAverage() + " | Total: " + stats.getCount());
				});
	}

	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		var fabricantes = listFabs.stream()
				.filter(fab -> fab.getProductos().size() >= 2)
				.map(Fabricante::getNombre)
				.toList();
		fabricantes.forEach(System.out::println);
		Assertions.assertFalse(fabricantes.isEmpty());
		Assertions.assertEquals(4, fabricantes.size());
	}

	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €.
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
		var resultado = listFabs.stream()
				.map(fab -> new Object[]{fab.getNombre(), fab.getProductos().stream().filter(p -> p.getPrecio() >= 220).count()})
				.sorted((a, b) -> Long.compare((Long) b[1], (Long) a[1]))
				.toList();
		resultado.forEach(res -> System.out.println(res[0] + " " + res[1]));
		Assertions.assertFalse(resultado.isEmpty());
	}

	/**
	 * 43. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €.
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		var fabricantes = listFabs.stream()
				.filter(fab -> fab.getProductos().stream().mapToDouble(Producto::getPrecio).sum() > 1000)
				.map(Fabricante::getNombre)
				.toList();
		fabricantes.forEach(System.out::println);
		Assertions.assertFalse(fabricantes.isEmpty());
	}

	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €.
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
		var fabricantes = listFabs.stream()
				.map(fab -> new Object[]{fab.getNombre(), fab.getProductos().stream().mapToDouble(Producto::getPrecio).sum()})
				.filter(res -> (Double) res[1] > 1000)
				.sorted(Comparator.comparingDouble(res -> (Double) res[1]))
				.toList();
		fabricantes.forEach(res -> System.out.println(res[0] + " " + res[1]));
		Assertions.assertFalse(fabricantes.isEmpty());
	}

	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante.
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante.
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		var resultado = listFabs.stream()
				.map(fab -> fab.getProductos().stream()
						.max(Comparator.comparingDouble(Producto::getPrecio))
						.map(producto -> String.format("%-20s %-10.2f %s", producto.getNombre(), producto.getPrecio(), fab.getNombre()))
						.orElse(String.format("%-20s %-10s %s", "Sin producto", "-", fab.getNombre())))
				.sorted()
				.toList();
		resultado.forEach(System.out::println);
		Assertions.assertFalse(resultado.isEmpty());
	}

	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();
		var resultado = listFabs.stream()
				.flatMap(fab -> {
					double mediaPrecio = fab.getProductos().stream().mapToDouble(Producto::getPrecio).average().orElse(0.0);
					return fab.getProductos().stream()
							.filter(p -> p.getPrecio() >= mediaPrecio)
							.sorted(Comparator.comparingDouble(Producto::getPrecio).reversed())
							.map(p -> String.format("%-20s %-10.2f %s", p.getNombre(), p.getPrecio(), fab.getNombre()));
				})
				.sorted()
				.toList();
		resultado.forEach(System.out::println);
		Assertions.assertFalse(resultado.isEmpty());
	}
}