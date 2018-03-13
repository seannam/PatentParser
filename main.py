import json
import psycopg2
from configparser import ConfigParser


def config(filename='database.ini', section='postgresql'):
    # create a parser
    parser = ConfigParser()
    # read config file
    parser.read(filename)

    # get section, default to postgresql
    db = {}
    if parser.has_section(section):
        params = parser.items(section)
        for param in params:
            db[param[0]] = param[1]
    else:
        raise Exception('Section {0} not found in the {1} file'.format(
                                                    section, filename))

    return db


def connect():
    """ Connect to the PostgreSQL database server """
    conn = None
    try:
        # read connection parameters
        params = config()

        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        conn = psycopg2.connect(**params)

        # create a cursor
        cur = conn.cursor()

        # execute a statement
        print('PostgreSQL database version:')
        cur.execute('SELECT version()')

        # display the PostgreSQL database server version
        db_version = cur.fetchone()
        print(db_version)

        # close the communication with the PostgreSQL
        cur.close()

    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()
            print('Database connection closed.')


# def main():
#     print("main function")
#     connect()
#     cited_by = {
#         "Patent ID": "",
#         "Publication number": "",
#         "Priority date": "",
#         "Publication date": "",
#         "Assignee": "",
#         "Title": ""
#     }

#     with open('data/citedby.json') as json_data:
#         citedby_json = json.load(json_data)

#     columns = cited_by.keys()
#     values = [cited_by[column] for column in columns]

    # insert_statement = 'insert into cited_by (%s) values %s'
    # print cursor.mogrify(insert_statement, (AsIs(','.join(columns)), tuple(values)))


def insert_citations_table_list(cits_list):
    sql = "INSERT INTO public.Citations(PatentID, PublicationNumber, PublicationDate, Assignee, Title) VALUES();"
    conn = None
    try:
        # read connection parameters
        params = config()

        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        conn = psycopg2.connect(**params)

        # create a cursor
        cur = conn.cursor()
        cur.executemany(sql, cits_list)

        # close the communication with the PostgreSQL
        cur.close()

    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()
            print('Database connection closed.')


if __name__ == '__main__':
    # connect()
    cits_list = [
        (1, 32, '2000-02-22', 'Tesla', 'model x'),
        (2, 22, '2020-04-22', 'Tesla', 'model y')
    ]
    insert_citations_table_list(cits_list)
