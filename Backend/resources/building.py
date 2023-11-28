from models.building import BuildingModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('name')

class CreateBuilding(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = BuildingModel(
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'success!' }, 200
        except:
            return {'message': 'Not created!'}, 400

class ReadBuilding(Resource):
    def get(self):
        return BuildingModel.return_all()
    
class DeleteBuilding(Resource):
    def delete(self):
        return BuildingModel.delete_one(request.args.get('id'))
    
class UpdateBuilding(Resource):
    def put(self):
        data = parser.parse_args()
        return BuildingModel.update_one(request.args.get('id'), data['name']) 