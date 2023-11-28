from models.floor import FloorModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('area_id')
parser.add_argument('name')

class CreateFloor(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = FloorModel(
            area_id = data['area_id'],
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'success!' }, 200
        except Exception as e:
            return {'message': str(e)}, 400

class ReadFloor(Resource):
    def get(self):
        return FloorModel.return_all_by_id(request.args.get('id'))
    
class DeleteFloor(Resource):
    def delete(self):
        return FloorModel.delete_one(request.args.get('id'))
    
class UpdateFloor(Resource):
    def put(self):
        data = parser.parse_args()
        return FloorModel.update_one(request.args.get('id'), data['name'], data['area_id'])
    
class ReadAllFloor(Resource):
    def get(self):
        return FloorModel.return_all()