
-- SEQUENCES --

CREATE SEQUENCE public.categoria_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.categoria_sequence
    OWNER TO postgres;	
	
-- ---------------------------------------------

CREATE SEQUENCE public.produto_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.produto_sequence
    OWNER TO postgres;
	
-- ---------------------------------------------

CREATE SEQUENCE public.usuario_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.usuario_sequence
    OWNER TO postgres;
    
-- ---------------------------------------------
    
CREATE TABLE public.usuario
(
    login character varying COLLATE pg_catalog."default",
    senha character varying COLLATE pg_catalog."default",
    id bigint NOT NULL DEFAULT nextval('usuario_sequence'::regclass),
    nome character varying(500) COLLATE pg_catalog."default",
    cep character varying(200) COLLATE pg_catalog."default",
    logradouro character varying(200) COLLATE pg_catalog."default",
    bairro character varying(200) COLLATE pg_catalog."default",
    cidade character varying(200) COLLATE pg_catalog."default",
    uf character varying(200) COLLATE pg_catalog."default",
    ibge character varying(200) COLLATE pg_catalog."default",
    fotobase64 text COLLATE pg_catalog."default",
    contenttype text COLLATE pg_catalog."default",
    contenttypecurriculo text COLLATE pg_catalog."default",
    curriculobase64 text COLLATE pg_catalog."default",
    fotobase64miniatura text COLLATE pg_catalog."default",
    ativo boolean,
    sexo character varying(50) COLLATE pg_catalog."default",
    perfil character varying(30) COLLATE pg_catalog."default",
    CONSTRAINT usuario_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.usuario
    OWNER to postgres;
    
-- ---------------------------------------------
    
ALTER TABLE usuario
    ADD COLUMN nome CHARACTER VARYING(100);
    
ALTER TABLE usuario
    ADD COLUMN telefone CHARACTER VARYING(50);
    
ALTER TABLE public.usuario
    ADD COLUMN id Serial PRIMARY KEY;
    
ALTER TABLE usuario
    ADD COLUMN cep CHARACTER VARYING(200);
    
ALTER TABLE usuario
    ADD COLUMN logradouro CHARACTER VARYING(200);
    
ALTER TABLE usuario
    ADD COLUMN bairro CHARACTER VARYING(200);
    
ALTER TABLE usuario
 	ADD COLUMN cidade CHARACTER VARYING(200);
    
ALTER TABLE usuario
    ADD COLUMN uf CHARACTER VARYING(200);
    
ALTER TABLE usuario
    ADD COLUMN ibge CHARACTER VARYING(200);
    
    
    
-- Table: public.produto

-- DROP TABLE public.produto;

CREATE TABLE public.produto
(
    id bigint NOT NULL DEFAULT nextval('produto_sequence'::regclass),
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    valor numeric(10,4) NOT NULL,
    quantidade bigint NOT NULL,
    CONSTRAINT produto_pkey PRIMARY KEY (id),
)

TABLESPACE pg_default;

ALTER TABLE public.produto
    OWNER to postgres;

-- ---------------------------------------------