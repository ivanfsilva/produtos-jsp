
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
    
    
    

ALTER TABLE usuario
    ADD COLUMN nome CHARACTER VARYING(100);
    
ALTER TABLE usuario
    ADD COLUMN telefone CHARACTER VARYING(50);
    
ALTER TABLE public.usuario
    ADD COLUMN id Serial PRIMARY KEY;
    
    
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