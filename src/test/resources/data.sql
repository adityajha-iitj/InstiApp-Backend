create extension hstore;

CREATE OR REPLACE FUNCTION get_user_name_from_user_ids(
    table_name TEXT,
    column_name TEXT,
    id_values BIGINT[]
)
    RETURNS TEXT[] AS $$
DECLARE
    result_array TEXT[];
BEGIN
    -- Avoid dynamic SQL here by directly using a static query
    SELECT ARRAY(SELECT user_name FROM users WHERE id = ANY(id_values))
    INTO result_array;
    RETURN result_array;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_(
    table_name TEXT,
    column_name TEXT,
    id_values BIGINT[]
)
    RETURNS TEXT[] AS $$
DECLARE
    result_array TEXT[];
BEGIN
    -- Avoid dynamic SQL here by directly using a static query
    SELECT ARRAY(SELECT user_name FROM users WHERE id = ANY(id_values))
    INTO result_array;
    RETURN result_array;
END;
$$ LANGUAGE plpgsql;
