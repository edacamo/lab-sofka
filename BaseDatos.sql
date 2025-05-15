/*Creacion tabla Persona*/
CREATE TABLE persona (
	 id SERIAL PRIMARY KEY,
	 nombre VARCHAR(100) NOT NULL,
	 genero VARCHAR(1),
	 edad INTEGER,
	 identificacion VARCHAR(13) UNIQUE NOT NULL,
	 direccion VARCHAR(255),
	 telefono VARCHAR(20),
	 CONSTRAINT ck_genero CHECK (genero IN ('M', 'F'))
);

/*Creacion tabla Cliente*/
CREATE TABLE cliente (
	 cliente_id VARCHAR(50) UNIQUE NOT NULL,
	 contrasenia VARCHAR(255) NOT NULL,
	 estado BOOLEAN,
	 persona_id INTEGER PRIMARY KEY,
	 CONSTRAINT fk_cliente_persona FOREIGN KEY (persona_id) REFERENCES persona (id)
);

/*Creacion tabla Cuentas*/
CREATE TABLE cuenta (
	id SERIAL PRIMARY KEY,
	numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
	tipo_cuenta VARCHAR(50),
	saldo_inicial NUMERIC,
	estado BOOLEAN,
	cliente_id VARCHAR,
	CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id)
);

/*Creacion tabla Movimientos*/
CREATE TABLE movimiento (
	id SERIAL PRIMARY KEY,
	fecha TIMESTAMP,
	tipo_movimiento VARCHAR(50),
	valor NUMERIC,
	saldo NUMERIC,
	cuenta_id INTEGER,
	CONSTRAINT ck_tipo_movimiento CHECK (tipo_movimiento IN ('C', 'D')),
	CONSTRAINT fk_movimiento_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta (id)
);
