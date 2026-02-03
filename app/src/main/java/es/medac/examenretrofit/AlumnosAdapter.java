package es.medac.examenretrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.AlumnoViewHolder> {

    private List<DatosAlumno> listaAlumnos;

    public AlumnosAdapter(List<DatosAlumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        DatosAlumno alumno = listaAlumnos.get(position);
        holder.tvNombre.setText(alumno.nombre);
        holder.tvNota.setText(alumno.nota);
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvNota;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreItem);
            tvNota = itemView.findViewById(R.id.tvNotaItem);
        }
    }
}