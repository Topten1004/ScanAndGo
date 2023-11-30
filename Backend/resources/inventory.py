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
parser.add_argument('comment')

class CreateInventory(Resource):
    def post(self):
        data = parser.parse_args()
        
        try:

            new_inventory = InventoryModel(

                category_id = data['category_id'],
                item_id = data['item_id'],
                building_id = data['building_id'],
                area_id = data['area_id'],
                floor_id = data['floor_id'],
                detail_location_id = data['detail_location_id'],
                barcode = data['barcode'],
                photo = data['photo'],
                status = data['status'],
                comment = data['comment']
            )
                
            new_inventory.save_to_db()
            return { 'message': 'success!' }, 200
        
        except Exception as e:
            print(e)
            return {'status': -1}, 500
        
class ReadInventory(Resource):
    def get(self):
        try:
            return InventoryModel.return_all()
        except:
            return {'status': -1}, 500
        
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