CREATE TABLE empresa_info (
id SERIAL PRIMARY KEY,
razonSocial VARCHAR(255) NOT NULL,
tipoDocumento VARCHAR(5) NOT NULL,
numeroDocumento VARCHAR(20) UNIQUE NOT NULL,
estado INT NOT NULL,
condicion VARCHAR(50) NOT NULL,
direccion TEXT,
distrito VARCHAR(100),
provincia VARCHAR(100),
departamento VARCHAR(100),
EsAgenteRetencion BOOLEAN NOT NULL DEFAULT false,
usuaCrea VARCHAR(255) NOT NULL,
dateCreate TIMESTAMP WITH TIME ZONE NOT NULL,
usuaModif VARCHAR(255),
dateModif TIMESTAMP WITH TIME ZONE,
usuaDelet VARCHAR(255),
dateDelet TIMESTAMP WITH TIME ZONE
);
-- Creación de la tabla persona con una columna para la clave foránea
CREATE TABLE persona (
id SERIAL PRIMARY KEY,
nombre VARCHAR(255) NOT NULL,
apellido VARCHAR(255) NOT NULL,
tipoDocumento VARCHAR(5) NOT NULL,
numeroDocumento VARCHAR(20) UNIQUE NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL,
telefono VARCHAR(15),
direccion TEXT,
estado INT NOT NULL,
usuaCrea VARCHAR(255) NOT NULL,
dateCreate TIMESTAMP WITH TIME ZONE NOT NULL,
usuaModif VARCHAR(255),
dateModif TIMESTAMP WITH TIME ZONE,
usuaDelet VARCHAR(255),
dateDelet TIMESTAMP WITH TIME ZONE,
empresa_id INT NOT NULL,
CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES
empresa_info(id)
);