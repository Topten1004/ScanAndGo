from models.inventory import InventoryModel
from models.category import CategoryModel
from models.item import ItemModel
from models.building import BuildingModel
from models.detaillocation import DetailLocationModel
from flask_restful import Resource, reqparse
from flask import request
import datetime

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('category_id')
parser.add_argument('item_id')
parser.add_argument('building_id')
parser.add_argument('area_id')
parser.add_argument('floor_id')
parser.add_argument('detail_location_id')
parser.add_argument('barcode')
parser.add_argument('status')
parser.add_argument('photo')

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
                            
            location = BuildingModel.find_by_name(data['location'])
            if (location):
                location_id = location.id
            else:
                new_location = BuildingModel(
                    name = data['location']
                )
                new_location.save_to_db()
                location_id = new_location.id
            
            sublocation = DetailLocationModel.find_by_id_name(location_id, data['sublocation'])
            if (sublocation):
                sublocation_id = sublocation.id
            else:
                new_sublocation = DetailLocationModel(
                    location_id = location_id,
                    name = data['sublocation']
                )
                new_sublocation.save_to_db()
                sublocation_id = new_sublocation.id
                
            new_inventory = InventoryModel(
                category_id = category_id,
                item_id = data['item_id'],
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
        
class ReadByDetailLocation(Resource):
    def get(self):
        try:
            return InventoryModel.return_all_by_detaillocation(request.args.get('id'))
        except: 
            return {'status': -1}, 200

class UpdateInventory(Resource):
    def put(self):
        data = parser.parse_args()
        return InventoryModel.updateInventory(request.args.get('id'), data['barcode'], data['status'], data['photo'])

class CreateNewInventory(Resource):
    def post(self):
        data = parser.parse_args()
        
        item = ItemModel.find_by_id(data['item_id'])
        
        new_inventory = InventoryModel(

            category_id = item.category_id,
            item_id = data['item_id'],
            building_id = data['location_id'],
            area_id = data['area_id'],
            floor_id = data['floor_id'],
            detail_location_id = data['detail_location_id'],

            reg_date = datetime.datetime.now().date()
        )
        try:
            new_inventory.save_to_db()
            return { 'message': 'success!' }, 200
        except:
            return { 'message': 'error!' }, 400