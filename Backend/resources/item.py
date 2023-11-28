from models.item import ItemModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('category_id')
parser.add_argument('name')
parser.add_argument('barcode')

class CreateItem(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = ItemModel(
            category_id = data['category_id'],
            name = data['name'],
            barcode = data['barcode']   
        )
        
        try:
            new_item.save_to_db()
            return { 'message': 'success!' }, 200
        except:
            return {'message': 'Not created!'}, 400

class ReadItem(Resource):
    def get(self):
        return ItemModel.return_all_by_id(request.args.get('id'))
    
class DeleteItem(Resource):
    def delete(self):
        return ItemModel.delete_one(request.args.get('id'))
    
class UpdateItem(Resource):
    def put(self):
        data = parser.parse_args()
        return ItemModel.update_one(request.args.get('id'), data['name'], data['category_id'], data['barcode'])
    
class ReadAllItem(Resource):
    def get(self):
        return ItemModel.return_all()
    
class AssignBarcode(Resource):
    def put(self):
        data = parser.parse_args()
        return ItemModel.assign_barcode(request.args.get('id'), data['barcode'])