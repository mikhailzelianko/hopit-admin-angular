import { ITrailPathWaypoint } from 'app/entities/trail-path-waypoint/trail-path-waypoint.model';
import { ITrail } from 'app/entities/trail/trail.model';

export interface ITrailPath {
  id?: number;
  title?: string;
  description?: string | null;
  trailPathWaypoints?: ITrailPathWaypoint[] | null;
  trail?: ITrail | null;
}

export class TrailPath implements ITrailPath {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public trailPathWaypoints?: ITrailPathWaypoint[] | null,
    public trail?: ITrail | null
  ) {}
}

export function getTrailPathIdentifier(trailPath: ITrailPath): number | undefined {
  return trailPath.id;
}
