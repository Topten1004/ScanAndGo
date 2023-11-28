from models.area import AreaModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('building_id')
parser.add_argument('name')

class CreateArea(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = AreaModel(
            buildingId = data['building_id'],
            name = data['name']
        )

        try:
            new_item.save_to_db()
            return { 'message': 'success!' }, 200
        except Exception as e:
            return {'message': str(e)}, 400

class ReadArea(Resource):
    def get(self):
        return AreaModel.return_all_by_id(request.args.get('id'))
    
class DeleteArea(Resource):
    def delete(self):
        return AreaModel.delete_one(request.args.get('id'))
    
class UpdateArea(Resource):
    def put(self):
        data = parser.parse_args()
        return AreaModel.update_one(request.args.get('id'), data['name'], data['building_id'])
    
class ReadAllArea(Resource):
    def get(self):
        return AreaModel.return_all()