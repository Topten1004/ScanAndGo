from models.inventory import InventoryModel
from models.category import CategoryModel
from models.subcategory import SubCategoryModel
from models.item import ItemModel
from models.location import LocationModel
from models.sublocation import SubLocationModel
from flask_restful import Resource, reqparse
from flask import request
import datetime

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('category')
parser.add_argument('category_id')
parser.add_argument('subcategory')
parser.add_argument('subcategory_id')
parser.add_argument('item')
parser.add_argument('item_id')
parser.add_argument('location')
parser.add_argument('location_id')
parser.add_argument('sublocation')
parser.add_argument('sublocation_id')
parser.add_argument('purchaseDate')
parser.add_argument('lastDate')
parser.add_argument('refClient')
parser.add_argument('regDate')

class CreateInventory(Resource):
    def post(self):
        data = parser.parse_args()
        
        try:
            category = CategoryModel.find_by_name(data['category'])
            if (category):
                category_id = category.id
            else:
                new_category = CategoryModel(
                    name = data['category']
                )
                new_category.save_to_db()
                category_id = new_category.id
                
            subcategory = SubCategoryModel.find_by_id_name(category_id, data['subcategory'])
            if (subcategory):
                subcategory_id = subcategory.id
            else:
                new_subcategory = SubCategoryModel(
                    category_id = category_id,
                    name = data['subcategory']
                )
                new_subcategory.save_to_db()
                subcategory_id = new_subcategory.id
                
            item = ItemModel.find_by_id_name(subcategory_id, data['item'])
            if (item):
                item_id = item.id
            else:
                new_item = ItemModel(
                    subcategory_id = subcategory_id,
                    name = data['item']
                )
                new_item.save_to_db()
                item_id = new_item.id
            
            location = LocationModel.find_by_name(data['location'])
            if (location):
                location_id = location.id
            else:
                new_location = LocationModel(
                    name = data['location']
                )
                new_location.save_to_db()
                location_id = new_location.id
            
            sublocation = SubLocationModel.find_by_id_name(location_id, data['sublocation'])
            if (sublocation):
                sublocation_id = sublocation.id
            else:
                new_sublocation = SubLocationModel(
                    location_id = location_id,
                    name = data['sublocation']
                )
                new_sublocation.save_to_db()
                sublocation_id = new_sublocation.id
                
            new_inventory = InventoryModel(
                category_id = category_id,
                subcategory_id = subcategory_id,
                item_id = item_id,
                location_id = location_id,
                sublocation_id = sublocation_id,
                purchase_date = data['purchaseDate'],
                last_date = data['lastDate'],
                ref_client = data['refClient']
            )
                
            new_inventory.save_to_db()
            return { 'status': 1 }, 200
        except:
            return {'status': -1}, 200
        
class ReadInventory(Resource):
    def get(self):
        try:
            return InventoryModel.return_all()
        except:
            return {'status': -1}, 200
        
class ReadBySubLocation(Resource):
    def get(self):
        try:
            return InventoryModel.return_all_by_sublocation(request.args.get('id'))
        except: 
            return {'status': -1}, 200

class CreateNewInventory(Resource):
    def post(self):
        data = parser.parse_args()
        
        item = ItemModel.find_by_id(data['item_id'])
        subcategory = SubCategoryModel.find_by_id(item.subcategory_id)
        
        new_inventory = InventoryModel(
            category_id = subcategory.category_id,
            subcategory_id = item.subcategory_id,
            item_id = data['item_id'],
            location_id = data['location_id'],
            sublocation_id = data['sublocation_id'],
            reg_date = datetime.datetime.now().date()
        )
        try:
            new_inventory.save_to_db()
            return { 'message': 'created!' }, 200
        except:
            return { 'message': 'error!' }, 400