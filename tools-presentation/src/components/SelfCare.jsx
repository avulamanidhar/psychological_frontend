import { useMemo, useState } from 'react'
import BottomNav from './BottomNav.jsx'
import WellDoneSlide from './WellDoneSlide.jsx'

const CHECKLIST_ITEMS = [
  'Drank enough water 💧',
  'Ate a nutritious meal 🥗',
  'Went outside or got fresh air ☀️',
  'Moved my body 🏃',
  'Connected with someone 💬',
  'Took a mindful break 🧘',
]

export default function SelfCare({ currentSlide, onNext, onPrevious }) {
  const [checked, setChecked] = useState({})
  const completedCount = useMemo(() => Object.values(checked).filter(Boolean).length, [checked])

  if (currentSlide === 0) {
    return (
      <div className="bg-bg-splash min-h-screen flex flex-col">
        <div className="px-6 pt-7">
          <button type="button" className="text-desc-gray text-[16px] font-medium" onClick={onPrevious}>
            ‹ Back
          </button>
          <div className="flex justify-between items-start mt-6">
            <div>
              <h1 className="text-title-blue text-[34px] font-extrabold leading-none">Self-Care</h1>
              <p className="text-desc-gray text-[14px] mt-1">Daily checklist</p>
            </div>
            <div className="text-right">
              <div className="text-button-blue text-[22px] font-extrabold leading-none">
                {completedCount}/6
              </div>
              <div className="text-desc-gray text-[12px] mt-1">completed</div>
            </div>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto px-6 mt-8 pb-4">
          {CHECKLIST_ITEMS.map((label, i) => (
            <div
              key={i}
              className="bg-white rounded-2xl px-5 py-4 mb-4 flex items-center cursor-pointer border border-stroke-gray/60 shadow-[0_6px_16px_rgba(26,35,126,0.04)]"
              onClick={() => setChecked((c) => ({ ...c, [i]: !c[i] }))}
            >
              <span className="w-6 h-6 rounded-full border-2 border-stroke-gray flex-shrink-0 flex items-center justify-center bg-white">
                {checked[i] ? <span className="text-button-blue text-sm font-bold">✓</span> : null}
              </span>
              <span className="ml-4 text-desc-gray text-[16px] flex-1">{label}</span>
            </div>
          ))}
        </div>

        <div className="px-6 pb-5 pt-2">
          <button
            type="button"
            onClick={onNext}
            className="w-full h-[58px] rounded-2xl bg-button-blue text-white text-[18px] font-bold shadow-[0_14px_28px_rgba(74,144,226,0.35)]"
          >
            Complete ✓
          </button>
        </div>

        <BottomNav />
      </div>
    )
  }

  if (currentSlide === 1) {
    return <WellDoneSlide exerciseName="Self-Care" onBackToTools={onNext} />
  }

  return null
}
