import mpi.*;

public class MPIAverage {
    public static void main(String args[]) throws Exception {

        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int n = 8;
        int chunkSize = n / size;

        int[] send = null;
        int[] recv = new int[chunkSize];

        if (rank == 0) {
            send = new int[]{1,2,3,4,5,6,7,8};
        }

        MPI.COMM_WORLD.Scatter(send, 0, chunkSize, MPI.INT,
                               recv, 0, chunkSize, MPI.INT, 0);

        double sum = 0;
        for (int i = 0; i < recv.length; i++)
            sum += recv[i];

        double localAvg = sum / recv.length;
        System.out.println("Process " + rank + " Local Avg: " + localAvg);

        double[] gathered = new double[size];

        MPI.COMM_WORLD.Gather(new double[]{localAvg}, 0, 1, MPI.DOUBLE,
                              gathered, 0, 1, MPI.DOUBLE, 0);

        if (rank == 0) {
            double finalSum = 0;
            for (int i = 0; i < size; i++)
                finalSum += gathered[i];

            double finalAvg = finalSum / size;
            System.out.println("Final Average: " + finalAvg);
        }

        MPI.Finalize();
    }
}