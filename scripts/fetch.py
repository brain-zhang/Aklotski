#coding='utf-8'
import io
import json
import requests
import urllib2
from BeautifulSoup import BeautifulSoup

def fetch_layout(id):
    url = "http://fayaa.com/youxi/hrd/%d/" % id
    page = urllib2.urlopen(url)
    soup = BeautifulSoup(page)
    x = json.loads(soup.find(attrs={'id':'canvas'})['tag'])
    return [[i,j,k] for i,j,k in zip(x[::3], x[1::3], x[2::3])]

def get_all_layouts():
    url = 'http://fayaa.com/youxi/hrd/gates/'
    result = requests.get(url).content
    return json.loads(result.lstrip('gates='))

if __name__ == '__main__':
    all_layouts = []
    for item in get_all_layouts():
    	item_layout = fetch_layout(item['id'])
        all_layouts.append({'id':item['id'], 'name':item['name'], 'layout':item_layout})
    final_results = json.dumps(all_layouts, ensure_ascii=False, indent=4)
    with io.open('layouts.json', 'w', encoding='utf-8') as f:
        f.write(final_results)
