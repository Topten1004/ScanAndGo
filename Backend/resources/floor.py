from models.floor import FloorModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('areaId')
parser.add_argument('name')

class CreateFloor(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = FloorModel(
            areaId = data['areaId'],
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'created!' }, 200
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
        return FloorModel.update_one(request.args.get('id'), data['name'])
    
class ReadAllFloor(Resource):
    def get(self):
        return FloorModel.return_all()