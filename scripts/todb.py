#coding='utf-8'
import io
import os
import json
import sqlite3

prj_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
db_file = os.path.join(prj_root, 'assets/aklotski.s3db')

levels = json.load(open(os.path.join(prj_root, 'scripts/levels.json'), 'r'))

def getlevel(id):
    for level in levels:
        if id in level['ids']:
            return level['level']
    return -1

if __name__ == '__main__':
    conn = sqlite3.connect(db_file)
    with io.open(os.path.join(prj_root, 'scripts/layouts.json'), 'r', encoding='utf-8') as fp:
        layouts = json.loads(fp.read())
        for layout in layouts:
            id = layout['id']
            name = layout['name']
            layout_des = layout['layout']
            layout_des = 'S'.join([''.join(str(item)[1:-1]) for item in layout_des])
            level = getlevel(id)
            print id, name, layout_des, level
            conn.execute("insert into wars(id,name,level,layout) values (%d, '%s', %d, '%s')" % (id, name, level, str(layout_des)))
        conn.commit()
