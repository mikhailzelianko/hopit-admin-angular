import { ITrail } from 'app/entities/trail/trail.model';

export interface IAdditionalMapObject {
  id?: number;
  title?: string;
  objectLat?: number | null;
  objectLong?: number | null;
  type?: string | null;
  description?: string | null;
  trail?: ITrail | null;
}

export class AdditionalMapObject implements IAdditionalMapObject {
  constructor(
    public id?: number,
    public title?: string,
    public objectLat?: number | null,
    public objectLong?: number | null,
    public type?: string | null,
    public description?: string | null,
    public trail?: ITrail | null
  ) {}
}

export function getAdditionalMapObjectIdentifier(additionalMapObject: IAdditionalMapObject): number | undefined {
  return additionalMapObject.id;
}
