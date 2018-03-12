import requests
import lxml.html
import psycopg2
import sys
from bs4 import BeautifulSoup
from lxml import etree, html
from configparser import ConfigParser


# patent = {}
# url = "https://patents.google.com/patent/US7302680"
# req = requests.get(url)

tree = html.fromstring(req.content)
text = tree.xpath('//*[@id="description"]')
# print(text)
 
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
        raise Exception('Section {0} not found in the {1} file'.format(section, filename))
 
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
 
def main():
    print("main function")
    connect()
    cited_by = {
      "current_patent_publication_number": ""
      "publication_number": "",
      "priority_date": "",
      "publication_date":, "",
      "assignee": "",
      "title": "",
    }

    columns = cited_by.keys()
    values = [cited_by[column] for column in columns]

    insert_statement = 'insert into cited_by (%s) values %s'
    print cursor.mogrify(insert_statement, (AsIs(','.join(columns)), tuple(values)))

    



# # ---------

# soup = BeautifulSoup(req.content, "lxml")

# all_div = soup.find_all("div")

# claims = soup.find_all("div", {"class": "claim"})
# claim_text = soup.find_all("claim-text")

# g_data = soup.find_all("div", {"class": "patent-result"})

# patent_id = soup.find_all("h2", {"class":"patent-result"})
# print(patent_id)

# description = soup.find_all("div", {"class": "text"})
# # print(description)

# cited = soup.findAll("Cited By")
# print(cited )
# # "tr style-scope patent-result"

# # ---------
# patent_text = soup.find_all("patent-text")
# # print(patent_text)

# title = soup.find("h1").text
# abstract = soup.find("div", {"class": "abstract"}).text

# patent["title"] = title
# patent["abstract"] = abstract


